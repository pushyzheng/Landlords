package site.pushy.landlords.common.component;

import org.springframework.stereotype.Component;
import site.pushy.landlords.pojo.Room;
import site.pushy.landlords.pojo.User;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Pushy
 * @since 2018/12/29 20:59
 */
@Component
public class RoomComponent {

    private ConcurrentHashMap<String, Room> roomMap = new ConcurrentHashMap<>();

    /**
     * 创建房间
     * @param user
     * @return
     */
    public Room createRoom(User user) {
        Room room = new Room();
        return room;
    }

    /**
     * 加入房间
     * @param id
     * @param user
     * @author fuxing
     */
    public boolean joinRoom(String id, User user) {
        return true;
    }

    /**
     * 退出房间
     * @param id
     * @param user
     * @return
     */
    public boolean exitRoom(String id, User user) {
        // if count == 0 => 解散
        return true;
    }

    /**
     * 发送消息给所有房间中的玩家
     */
    public boolean sendMessageToRoom(String id, String message) {
        return false;
    }


}
