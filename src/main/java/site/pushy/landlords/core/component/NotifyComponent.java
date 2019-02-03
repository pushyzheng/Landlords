package site.pushy.landlords.core.component;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import site.pushy.landlords.common.handler.WebSocketPushHandler;
import site.pushy.landlords.common.util.RespEntity;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.Player;
import site.pushy.landlords.pojo.Room;
import site.pushy.landlords.pojo.ws.Message;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Fuxing
 * @since 2019/1/2 20:11
 */
@Component
public class NotifyComponent {

    @Autowired
    private WebSocketPushHandler webSocketHandler;

    @Autowired
    private RoomComponent roomComponent;

    /**
     * 发送给某个房间所有的客户端消息
     * @return 是否发送成功，一旦一个客户端发送不成功，则视为不成功
     */
    public boolean sendToAllUserOfRoom(String roomId, String content) {
        Room room = roomComponent.getRoom(roomId);
        List<String> userIdList = new LinkedList<>();
        for (User user : room.getUserList()) {
            userIdList.add(user.getId());
        }
        return webSocketHandler.sendToUsers(userIdList, content);
    }

    public boolean sendToAllUserOfRoom(String roomId, Message message) {
        String content = JSON.toJSONString(message);
        return this.sendToAllUserOfRoom(roomId, content);
    }

    /**
     * 发送给某个玩家的客户端消息
     * @param userId
     * @param content
     */
    public boolean sendToUser(String userId, String content) {
        return webSocketHandler.sendToUser(userId, content);
    }

    public boolean sendToUser(String userId, Message message) {
        String content = JSON.toJSONString(message);
        return webSocketHandler.sendToUser(userId, content);
    }


}
