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

    private String id;                      // 房间号

    private String title;

    private UserOutDTO owner;

    private List<PlayerOutDTO> playerList;  // 当前玩家列表

    private RoomStatusEnum status;          // 房间的状态

    private int multiple;                   // 房间底分

    private List<Card> topCards;            // 三张底牌

    private int stepNum;              // 每局走的步数，用来控制玩家的出牌回合

    public RoomOutDTO(Room room) {
        id = room.getId();
        title = room.getTitle();
        owner = UserOutDTO.fromUser(room.getOwner());
        playerList = room.getPlayerList().stream()
                .map(PlayerOutDTO::new)
                .collect(Collectors.toList());
        status = room.getStatus();
        multiple = room.getMultiple();
        if (room.getDistribution() != null) {
            topCards = room.getDistribution().getTopCards();
        }
        stepNum = room.getStepNum();
    }
}
