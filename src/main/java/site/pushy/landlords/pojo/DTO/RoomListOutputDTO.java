package site.pushy.landlords.pojo.DTO;

import lombok.Data;
import site.pushy.landlords.core.enums.RoomStatusEnum;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.Room;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pushy
 * @since 2019/2/6 17:53
 */
@Data
public class RoomListOutputDTO {

    private String id;                // 房间号

    private boolean locked;           // 房间是否设置密码,true为设置

    private List<UserOutDTO> userList;      // 当前用户列表

    private RoomStatusEnum status;    // 房间的状态

    public RoomListOutputDTO(Room room) {
        id = room.getId();
        locked = room.isLocked();
        userList = room.getUserList().stream()
                .map(UserOutDTO::new)
                .collect(Collectors.toList());
        status = room.getStatus();
    }

    public String getStatusValue() {
        return status != null ? status.value() : "";
    }

}
