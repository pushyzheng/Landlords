package site.pushy.landlords.common.handler;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import site.pushy.landlords.pojo.ws.PongMessage;

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

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final ConcurrentHashMap<String, WebSocketSession> userMap =
            new ConcurrentHashMap<>();

    /**
     * 在这个方法内来记录用户标识，从之前的WebSocketInterceptor拦截器添加的attributes中取出用户userId
     * 然后将该用户的id作为键，当前链接客户端的session作为值映射到usersMap当中
     * @author Fuxing
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = (String) session.getAttributes().get("userId");
        userMap.put(userId, session);
    }

    /**
     * 处理客户端通过webSocket.send()发送的消息，这里用来处理心跳检测
     */
    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> message) throws Exception {
        String payload = (String) message.getPayload();
        if (payload.equals("ping")) {
            String respContent = JSON.toJSONString(new PongMessage());
            webSocketSession.sendMessage(new TextMessage(respContent));
        }
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {

    }

    /**
     * 用户退出后的处理，当用户关闭连接后，将session关闭后，并将usersMap的session给remove掉
     * 这样用户就处于离线状态了，也不会占用系统资源
     * @author Fuxing
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        String userId = (String) session.getAttributes().get("userId");
        //logger.info(String.format("Client 【%s】 closed webSocket connection", userId));
        userMap.remove(userId);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 给所有的用户发送消息
     * @author Pushy
     */
    public void sendToAllUser(String content) {
        TextMessage message = new TextMessage(content.getBytes());

        for (Map.Entry<String,WebSocketSession> entry : userMap.entrySet()) {
            WebSocketSession session = entry.getValue();
            if (!session.isOpen()) {
                logger.error("玩家不在线");
            }
            else {
                try {
                    session.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 发送消息给指定的用户
     * @return 是否发送成功
     * @author Pushy
     */
    public boolean sendToUser(String userId, String content) {
        TextMessage message = new TextMessage(content.getBytes());
        WebSocketSession session = userMap.get(userId);

        if (session == null) {
            return false;
        }
        try {
            if (!session.isOpen()) {
                logger.error("用户不在线");
                return false;
            }
            session.sendMessage(message);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 发送给列表中的用户
     * @author Pushy
     */
    public boolean sendToUsers(List<String> userIdList, String content) {
        boolean res = true;
        for (String userId : userIdList) {
            res = sendToUser(userId, content);
        }
        return res;
    }

}
