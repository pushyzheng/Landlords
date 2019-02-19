package site.pushy.landlords.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.pushy.landlords.common.util.RespEntity;
import site.pushy.landlords.core.component.RoomComponent;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.DTO.RoomDTO;
import site.pushy.landlords.pojo.DTO.RoomListOutputDTO;
import site.pushy.landlords.pojo.Room;
import site.pushy.landlords.service.RoomService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fuxing
 * @since 2018/12/29 20:34
 */
@RestController
@RequestMapping(value = "/rooms", produces = "application/json")
public class RoomController {

    @Autowired
    private RoomComponent roomComponent;

    @Autowired
    private RoomService roomService;

    /**
     * 获取所有房间列表
     * @return
     */
    @GetMapping("")
    public String listRoom() {
        List<Room> roomList = roomComponent.getRooms();
        if (roomList.size() != 0) {
            List<RoomListOutputDTO> dtoList = roomList.stream()
                    .map(RoomListOutputDTO::new)
                    .collect(Collectors.toList());
            return RespEntity.success(dtoList);
        }
        return RespEntity.success(roomList);
    }

    /**
     * 通过房间号id查看某房间的所有信息，该玩家必须在该房间内
     * @author Pushy
     */
    @GetMapping("/{id}")
    public String getRoomById(@PathVariable String id,
                              @SessionAttribute User curUser) {
        return RespEntity.success(roomService.getRoomById(curUser, id));
    }

    /**
     * 创建房间
     */
    @PostMapping("")
    public String createRoom(@SessionAttribute User curUser,
                             @RequestBody JSONObject body) {
        return RespEntity.success(roomService.createRoom(curUser, body.getString("password"),
                body.getString("title")));
    }

    /**
     * 加入房间
     */
    @PostMapping("/join")
    public String joinRoom(@Valid @RequestBody RoomDTO roomDTO,
                           @SessionAttribute User curUser) {
        return RespEntity.success(roomService.joinRoom(curUser, roomDTO));
    }

    /**
     * 退出房间
     */
    @PostMapping("/exit")
    public String exitRoom(@SessionAttribute User curUser) {
        roomService.exitRoom(curUser);
        return RespEntity.success("success");
    }

}
