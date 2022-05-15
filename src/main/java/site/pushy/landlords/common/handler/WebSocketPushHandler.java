package site.pushy.landlords.common.handler;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import site.pushy.landlords.common.config.properties.LandlordsProperties;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.ws.PongMessage;
import site.pushy.landlords.service.RoomService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Fuxing
 * @since 2019/1/2 20:12
 */
@Component
public class WebSocketPushHandler implements WebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketPushHandler.class);

    private static final ConcurrentHashMap<String, Connection> connections =
            new ConcurrentHashMap<>();

    @Resource
    private LandlordsProperties properties;

    @Resource
    private RoomService roomService;

    @Scheduled(fixedRate = 10000)
    public synchronized void detectOffline() {
        logger.info("开始检测用户的心跳状态");
        connections.forEach((userId, conn) -> {
            if (heartBeatTimeout(conn)) {
                handleOffline(userId);
            }
        });
    }

    /**
     * 在这个方法内来记录用户标识，从之前的WebSocketInterceptor拦截器添加的attributes中取出用户userId
     * 然后将该用户的id作为键，当前链接客户端的session作为值映射到usersMap当中
     *
     * @author Fuxing
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String userId = (String) session.getAttributes().get("userId");
        connections.put(userId, new Connection(session));
    }

    /**
     * 处理客户端通过webSocket.send()发送的消息，这里用来处理心跳检测
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String payload = (String) message.getPayload();
        logger.info("handleMessage, payload: {}", payload);
        if (payload.equalsIgnoreCase("ping")) {
            handlePing(session);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {

    }

    /**
     * 用户退出后的处理，当用户关闭连接后，将session关闭后，并将usersMap的session给remove掉
     * 这样用户就处于离线状态了，也不会占用系统资源
     *
     * @author Fuxing
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        logger.info("[afterConnectionClosed] sessionId: {}, closeStatus: {}", session.getId(), closeStatus);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public boolean isOnline(String userId) {
        if (!connections.containsKey(userId)) {
            return false;
        }
        Connection conn = connections.get(userId);
        return conn.session.isOpen();
    }

    /**
     * 给所有的用户发送消息
     *
     * @author Pushy
     */
    public int sendToAllUser(String content) {
        TextMessage message = new TextMessage(content.getBytes());

        int fail = 0;
        for (Map.Entry<String, Connection> entry : connections.entrySet()) {
            WebSocketSession session = entry.getValue().session;
            if (!session.isOpen()) {
                logger.warn("sendToAllUser 玩家不在线: " + entry.getKey());
            } else {
                try {
                    session.sendMessage(message);
                } catch (IOException e) {
                    logger.error("sendToAllUser 推送消息异常: {}", entry.getKey(), e);
                }
            }
        }
        return fail;
    }

    /**
     * 发送消息给指定的用户
     *
     * @return 是否发送成功
     * @author Pushy
     */
    public boolean sendToUser(String userId, String content) {
        Connection conn = connections.get(userId);
        WebSocketSession session;
        if (conn == null || (session = conn.session) == null || !session.isOpen()) {
            logger.warn("用户不在线: {}", userId);
            return false;
        }
        TextMessage message = new TextMessage(content.getBytes());
        try {
            session.sendMessage(message);
            return true;
        } catch (IOException e) {
            logger.error("推送异常, userId: {}", userId, e);
            return false;
        }
    }

    /**
     * 发送给列表中的用户
     *
     * @author Pushy
     */
    public boolean sendToUsers(List<String> userIdList, String content) {
        boolean res = true;
        for (String userId : userIdList) {
            res = sendToUser(userId, content);
        }
        return res;
    }

    private void handlePing(WebSocketSession session) throws IOException {
        connections.forEach((userId, conn) -> {
            if (conn.session.getId().equals(session.getId())) {
                conn.lastPingTime = System.currentTimeMillis();
            }
        });
        // Reply PONG message
        String respContent = JSON.toJSONString(new PongMessage());
        session.sendMessage(new TextMessage(respContent));
    }

    private void handleOffline(String userId) {
        connections.remove(userId);
        logger.info("心跳检测时间超时, 移除该用户: {}", userId);
        try {
            roomService.exitRoom(new User().setId(userId));
        } catch (Exception e) {
            // ignore the exception
        }
    }

    private boolean heartBeatTimeout(Connection conn) {
        return System.currentTimeMillis() - conn.lastPingTime
                > properties.getHeartbeatTimeout().toMillis();
    }

    /**
     * 客户端连接信息
     */
    private static class Connection {

        WebSocketSession session;

        /**
         * 登录的时间
         */
        long loginTime;

        /**
         * 上一次心跳检测的时间戳
         */
        long lastPingTime;

        public Connection(WebSocketSession session) {
            this.session = session;
            this.lastPingTime = System.currentTimeMillis();
            this.loginTime = System.currentTimeMillis();
        }
    }
}
