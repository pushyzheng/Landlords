package site.pushy.landlords.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.pushy.landlords.common.exception.ForbiddenException;
import site.pushy.landlords.common.util.RespEntity;
import site.pushy.landlords.core.component.RoomComponent;
import site.pushy.landlords.core.enums.RoomStatusEnum;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.DTO.RoomDTO;
import site.pushy.landlords.pojo.Room;

import javax.validation.Valid;

/**
 * @author Fuxing
 * @since 2018/12/29 20:34
 */
@RestController
@RequestMapping(value = "/rooms", produces = "application/json")
public class RoomController {

    @Autowired
    private RoomComponent roomComponent;

    /**
     * 获取所有房间列表
     * @return
     */
    @GetMapping("")
    public String listRoom() {
        return RespEntity.success(roomComponent.getRooms());
    }

    /**
     * 通过房间号id查看某房间的所有信息，该玩家必须在该房间内
     * @author Pushy
     */
    @GetMapping("/{id}")
    public String getRoomById(@PathVariable String id,
                              @SessionAttribute User curUser) {
        Room room = roomComponent.getRoom(id);
        boolean canRead = false;
        for (User user : room.getUserList()) {
            if (curUser.getId().equals(user.getId())) {
                canRead = true;
                break;
            }
            continue;
        }
        if (!canRead){
            throw new ForbiddenException("你无权查看本房间的信息");
        }
        return RespEntity.success(roomComponent.getRoom(id));
    }

    /**
     * 创建房间
     */
    @PostMapping("")
    public String createRoom(@SessionAttribute User curUser,
                             @RequestBody JSONObject body) {
        return RespEntity.success(roomComponent.createRoom(curUser, body.getString("password")));
    }

    /**
     * 加入房间
     */
    @PostMapping("/join")
    public String joinRoom(@Valid @RequestBody RoomDTO roomDTO,
                           @SessionAttribute User curUser) {
        String roomId = roomDTO.getId();
        Room room = roomComponent.getRoom(roomDTO.getId());
        if (room.getStatus() == RoomStatusEnum.PLAYING) {
            throw new ForbiddenException("房间正在游戏中，无法加入!");
        }
        String roomPassword = roomDTO.getPassword();
        String message = roomComponent.joinRoom(roomId, curUser, roomPassword);
        return RespEntity.success(message);
    }

    /**
     * 退出房间
     */
    @PostMapping("/exit")
    public String exitRoom(@Valid @RequestBody RoomDTO roomDTO,
                           @SessionAttribute User curUser) {
        String roomId = roomDTO.getId();
        String message = roomComponent.exitRoom(roomId, curUser);
        return RespEntity.success(message);
    }

    /**
     * 获取下家
     */
    @GetMapping("/next")
    public String getNextPlayer(@Valid @RequestBody RoomDTO roomDTO,
                                @SessionAttribute User curUser) {
        String roomid = roomDTO.getId();
        return RespEntity.success(roomComponent.getNextPlayer(roomid, curUser));
    }

}
