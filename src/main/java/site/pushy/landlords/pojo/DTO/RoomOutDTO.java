package site.pushy.landlords.pojo.DTO;

import lombok.Data;
import site.pushy.landlords.core.enums.RoomStatusEnum;
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

    private List<PlayerOutDTO> playerList;  // 当前玩家列表

    private RoomStatusEnum status;          // 房间的状态

    private int multiple;                   // 房间底分

    public RoomOutDTO(Room room) {
        id = room.getId();
        playerList = room.getPlayerList().stream()
                .map(PlayerOutDTO::new)
                .collect(Collectors.toList());
        status = room.getStatus();
        multiple = room.getMultiple();
    }
}
