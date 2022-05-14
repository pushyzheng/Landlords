package site.pushy.landlords.pojo;

import lombok.Data;
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

    /**
     * 房间号
     */
    private String id;

    /**
     * 房间密码
     */
    private String password;

    /**
     * 房间标题
     */
    private String title;

    /**
     * 房主用户对象
     */
    private User owner;

    /**
     * 房间是否设置密码,true为设置
     */
    private boolean locked;

    /**
     * 当前玩家列表
     */
    private List<Player> playerList;

    /**
     * 当前用户列表
     */
    private List<User> userList;

    /**
     * 房间的状态
     */
    private RoomStatusEnum status;

    /**
     * 房间底分
     */
    private int multiple;

    /**
     * 每局走的步数，用来控制玩家的出牌回合
     */
    private int stepNum;

    /**
     * 叫牌的玩家
     */
    private int biddingPlayer;

    /**
     * 上一回合玩家打出的牌
     */
    private List<Card> preCards;

    /**
     * 上一回合玩家的 playerId
     */
    private int prePlayerId;

    /**
     * 上一回合玩家出牌的时间戳
     */
    private long prePlayTime;

    private CardDistribution distribution;

    public String getStatusValue() {
        return status != null ? status.getValue() : "";
    }

    public Room() {
        this.locked = false;
        this.status = RoomStatusEnum.PREPARING;
        this.playerList = new ArrayList<>();
        this.userList = new ArrayList<>();
        this.multiple = 1;
        this.prePlayerId = 0;
        this.stepNum = -1;   // 当step = -1时代表叫牌还未结束
        this.biddingPlayer = -1;
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
        this.status = RoomStatusEnum.PREPARING;
        this.preCards = null;
        this.prePlayerId = 0;
        this.stepNum = -1;
        this.biddingPlayer = -1;
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

    public void incrBiddingPlayer() {
        if (biddingPlayer == 3) biddingPlayer = 1;
        else biddingPlayer++;
    }

    public void doubleMultiple() {
        multiple *= 2;
    }

    /**
     * 通过userId获取Player对象
     */
    public Player getPlayerByUserId(String userId) {
        for (Player player : playerList) {
            if (player.getUser().getId().equals(userId)) {
                return player;
            }
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
