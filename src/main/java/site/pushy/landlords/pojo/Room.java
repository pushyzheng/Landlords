package site.pushy.landlords.pojo;

import lombok.Data;
import org.springframework.web.socket.WebSocketSession;
import site.pushy.landlords.core.enums.RoomStatusEnum;
import site.pushy.landlords.pojo.DO.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pushy
 * @since 2018/12/29 20:44
 */
@Data
public class Room {

    private String id;                // 房间号

    private String password;          // 房间密码

    private boolean locked;           // 房间是否设置密码,true为设置

    private List<Player> playerList;  // 当前玩家列表

    private List<User> userList;      // 当前用户列表

    private RoomStatusEnum status;    // 房间的状态

    private List<WebSocketSession> userSessionList;  // 当家玩家客户端Session对象列表

    public String getStatusValue() {
        return status != null ? status.value() : "";
    }

    public Room() {
        this.locked = false;
        this.status = RoomStatusEnum.PREPARING;
        this.playerList = new ArrayList<>();
        this.userList = new ArrayList<>();
        this.userSessionList = new ArrayList<>();
    }

    public Room(String id) {
        this();
        this.id = id;
    }

    public void addPlayer(Player player) {
        this.playerList.add(player);
    }

    public void addUser(User user) {
        this.userList.add(user);
    }


}
