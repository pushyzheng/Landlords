package site.pushy.landlords.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.pushy.landlords.common.exception.ForbiddenException;
import site.pushy.landlords.core.component.NotifyComponent;
import site.pushy.landlords.core.component.RoomComponent;
import site.pushy.landlords.core.enums.RoomStatusEnum;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.DTO.RoomDTO;
import site.pushy.landlords.pojo.DTO.RoomOutDTO;
import site.pushy.landlords.pojo.Room;
import site.pushy.landlords.pojo.ws.Message;
import site.pushy.landlords.pojo.ws.PlayerExitMessage;
import site.pushy.landlords.pojo.ws.PlayerJoinMessage;
import site.pushy.landlords.service.RoomService;

/**
 * @author Pushy
 * @since 2019/2/15 22:39
 */
@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomComponent roomComponent;

    @Autowired
    private NotifyComponent notifyComponent;

    @Override
    public RoomOutDTO getRoomById(User curUser, String id) {
        Room room = roomComponent.getRoom(id);
        boolean canRead = false;
        for (User user : room.getUserList()) {
            if (curUser.getId().equals(user.getId())) {
                canRead = true;
                break;
            }
        }
        if (!canRead){
            throw new ForbiddenException("你无权查看本房间的信息");
        }
        return new RoomOutDTO(room);
    }

    @Override
    public Room createRoom(User curUser, String password, String title) {
        return roomComponent.createRoom(curUser, password, title);
    }

    @Override
    public String joinRoom(User curUser, RoomDTO roomDTO) {
        String roomId = roomDTO.getId();
        Room room = roomComponent.getRoom(roomDTO.getId());
        if (room.getStatus() == RoomStatusEnum.PLAYING) {
            throw new ForbiddenException("房间正在游戏中，无法加入!");
        }
        String roomPassword = roomDTO.getPassword();
        String message = roomComponent.joinRoom(roomId, curUser, roomPassword);

        /* 通知房间内的玩家客户端有新的玩家加入 */
        Message playerJoinMessage = new PlayerJoinMessage(curUser);
        notifyComponent.sendToAllUserOfRoom(roomId, playerJoinMessage);

        return message;
    }

    @Override
    public void exitRoom(User curUser) {
        Room room = roomComponent.getUserRoom(curUser.getId());
        boolean hasRemove = roomComponent.exitRoom(room.getId(), curUser);
        if (!hasRemove) {
            /* 通知房间内的玩家客户端有玩家退出 */
            Message playerJoinMessage = new PlayerExitMessage(curUser);
            notifyComponent.sendToAllUserOfRoom(room.getId(), playerJoinMessage);
        }
    }
}
