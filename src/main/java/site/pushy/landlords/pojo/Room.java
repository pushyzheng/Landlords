package site.pushy.landlords.pojo;

import lombok.Data;
import org.springframework.web.socket.WebSocketSession;
import site.pushy.landlords.core.CardDistribution;
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

    private String title;             // 房间标题

    private boolean locked;           // 房间是否设置密码,true为设置

    private List<Player> playerList;  // 当前玩家列表

    private List<User> userList;      // 当前用户列表

    private RoomStatusEnum status;    // 房间的状态

    private int multiple;             // 房间底分

    private int stepNum;              // 每局走的步数，用来控制玩家的出牌回合

    private List<Card> preCards;      // 上一回合玩家打出的牌

    private int prePlayerId;          // 上一回合玩家的playerId

    private CardDistribution distribution;

    public String getStatusValue() {
        return status != null ? status.value() : "";
    }

    public Room() {
        this.locked = false;
        this.status = RoomStatusEnum.PREPARING;
        this.playerList = new ArrayList<>();
        this.userList = new ArrayList<>();
        this.multiple = 1;
        this.stepNum = 1;
        this.prePlayerId = 0;
    }

    public Room(String id) {
        this();
        this.id = id;
    }

    /**
     * 每次每局结束之后，初始化Room对象的部分参数值
     */
    public void reset() {
        this.multiple = 1;
        this.stepNum = 1;
        this.status = RoomStatusEnum.PREPARING;
        this.preCards = null;
        this.prePlayerId = 0;
        // 初始化Player对象的值
        for (Player player : playerList) {
            player.reset();
        }
    }

    public void addPlayer(Player player) {
        this.playerList.add(player);
    }

    public void removePlayer(String userId) {
        for (Player player : playerList) {
            if (player.getUser().getId().equals(userId)) {
                playerList.remove(player);
                break;
            }
        }
    }

    public void addUser(User user) {
        this.userList.add(user);
    }

    public void removeUser(String userId) {
        for (User user : userList) {
            if (user.getId().equals(userId)) {
                userList.remove(user);
                break;
            }
        }
    }

    public void incrStep() {
        stepNum++;
    }

    public void doubleMultiple() {
        multiple *= 2;
    }

    /**
     * 通过userId获取Player对象
     */
    public Player getPlayerByUserId(String userId) {
        for (Player player : playerList) {
            if (player.getUser().getId().equals(userId)) return player;
        }
        return null;
    }

    /**
     * 通过玩家的序号获取对应的Player对象
     */
    public Player getPlayerById(int playerId) {
        for (Player player : playerList) {
            if (player.getId() == playerId) return player;
        }
        return null;
    }

    /**
     * 通过玩家序号获取关联的User对象
     */
    public User getUserByPlayerId(int playerId) {
        Player player = getPlayerById(playerId);
        return player.getUser();
    }

}
