package site.pushy.landlords.core.component;

import org.springframework.stereotype.Component;
import site.pushy.landlords.common.handler.WebSocketPushHandler;
import site.pushy.landlords.common.util.JsonUtils;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.Room;
import site.pushy.landlords.pojo.ws.Message;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 通知模块, 通过 WebSocket 实时地给房间内的客户端推送消息
 *
 * @author Pushy
 * @since 2019/1/2 20:11
 */
@Component
public class NotifyComponent {

    @Resource
    private WebSocketPushHandler webSocketHandler;

    @Resource
    private RoomComponent roomComponent;

    /**
     * 发送给某个房间所有的客户端消息
     *
     * @return 是否发送成功，一旦一个客户端发送不成功，则视为不成功
     */
    public boolean sendToAllUserOfRoom(String roomId, String content) {
        Room room = roomComponent.getRoom(roomId);
        List<String> userIdList = room.getUserList().stream()
                .filter(Objects::nonNull)
                .map(User::getId)
                .collect(Collectors.toList());
        return webSocketHandler.sendToUsers(userIdList, content);
    }

    public boolean sendToAllUserOfRoom(String roomId, Message message) {
        String content = JsonUtils.toJson(message);
        return sendToAllUserOfRoom(roomId, content);
    }

    /**
     * 发送给某个玩家的客户端消息
     *
     * @param userId
     * @param content
     */
    public boolean sendToUser(String userId, String content) {
        return webSocketHandler.sendToUser(userId, content);
    }

    public boolean sendToUser(String userId, Message message) {
        String content = JsonUtils.toJson(message);
        return webSocketHandler.sendToUser(userId, content);
    }

    public int sendToAllUser(Message message) {
        return webSocketHandler.sendToAllUser(JsonUtils.toJson(message));
    }
}
