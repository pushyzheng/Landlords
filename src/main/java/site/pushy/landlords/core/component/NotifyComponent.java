package site.pushy.landlords.core.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import site.pushy.landlords.common.handler.WebSocketPushHandler;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.Room;

/**
 * @author Fuxing
 * @since 2019/1/2 20:11
 */
@Component
public class NotifyComponent {

    @Autowired
    private WebSocketPushHandler websocketHandler;

    @Autowired
    private RoomComponent roomComponent;

    /*
        1. 发送给某个房间所有的客户端消息
        2. 发送给某个玩家的客户端消息
     */

    //发送给某个房间所有的客户端消息
    public void toAll(String id,TextMessage message){
        for (Room room:roomComponent.getRooms()
             ) {
            if(room.getId().equals(id)){
                //得到这个房间的所有用户
                for (User user:room.getUserList()
                     ) {
                    String userId = user.getId();
                    websocketHandler.sendMessageToUser(userId,message);
                }
            }
        }
    }

    //发送给某个玩家的客户端消息
    public void toOne(String userId, TextMessage message){
        websocketHandler.sendMessageToUser(userId,message);
    }


}
