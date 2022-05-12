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
     * 房间是否设置密码,true为设置
     */
    private boolean locked;

    /**
     * 当前用户列表
     */
    private List<UserOutDTO> userList;

    /**
     * 房间的状态
     */
    private RoomStatusEnum status;

    public RoomListOutputDTO(Room room) {
        id = room.getId();
        title = room.getTitle();
        owner = new UserOutDTO(room.getOwner());
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
