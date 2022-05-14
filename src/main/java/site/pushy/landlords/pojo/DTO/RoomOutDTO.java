package site.pushy.landlords.pojo.DTO;

import lombok.Data;
import site.pushy.landlords.core.enums.RoomStatusEnum;
import site.pushy.landlords.pojo.Card;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.Player;
import site.pushy.landlords.pojo.Room;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pushy
 * @since 2019/2/15 23:46
 */
@Data
public class RoomOutDTO {

    /**
     * 房间号
     */
    private String id;

    /**
     * 房间标题
     */
    private String title;

    /**
     * 房主
     */
    private UserOutDTO owner;

    /**
     * 当前玩家列表
     */
    private List<PlayerOutDTO> playerList;

    /**
     * 房间的状态
     */
    private RoomStatusEnum status;

    /**
     * 房间底分
     */
    private int multiple;

    /**
     * 三张底牌
     */
    private List<Card> topCards;

    /**
     * 每局走的步数，用来控制玩家的出牌回合
     */
    private int stepNum;

    /**
     * 计时器
     */
    private int countdown;

    public static RoomOutDTO fromRoom(Room room) {
        RoomOutDTO roomOutDTO = new RoomOutDTO();
        roomOutDTO.id = room.getId();
        roomOutDTO.title = room.getTitle();
        roomOutDTO.owner = UserOutDTO.fromUser(room.getOwner());
        roomOutDTO.playerList = room.getPlayerList().stream()
                .map(PlayerOutDTO::new)
                .collect(Collectors.toList());
        roomOutDTO.status = room.getStatus();
        roomOutDTO.multiple = room.getMultiple();
        if (room.getDistribution() != null) {
            roomOutDTO.topCards = room.getDistribution().getTopCards();
        }
        roomOutDTO.stepNum = room.getStepNum();
        return roomOutDTO;
    }
}
