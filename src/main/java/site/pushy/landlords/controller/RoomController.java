package site.pushy.landlords.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.pushy.landlords.common.util.RespEntity;
import site.pushy.landlords.core.component.RoomComponent;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.DTO.ReqRoom;

/**
 * @author Fuxing
 * @since 2018/12/29 20:34
 */
@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomComponent roomComponent;

    //获取所有房间列表
    @GetMapping("")
    public String listRoom() {
        return RespEntity.success(roomComponent.getRooms());
    }

    //创建房间
    @PostMapping("")
    public String createRoom(@SessionAttribute User user,@RequestBody ReqRoom reqRoom){
        String roomPassword = reqRoom.getPassword();
        return RespEntity.success(roomComponent.createRoom(user,roomPassword));
    }

    //加入房间
    @PostMapping("/join")
    public String joinRoom(@RequestBody ReqRoom reqRoom,@SessionAttribute User user){
        String roomid = reqRoom.getId();
        String roomPassword = reqRoom.getPassword();
        String message = roomComponent.joinRoom(roomid,user,roomPassword);
        return RespEntity.success(message);
    }

    //退出房间
    @PostMapping("/exit")
    public String exitRoom(@RequestBody ReqRoom reqRoom,@SessionAttribute User user){
        String roomid = reqRoom.getId();
        String message = roomComponent.exitRoom(roomid,user);
        return RespEntity.success(message);
    }

    //获取下家
    @GetMapping("/next")
    public String getNextPlayer(@RequestBody ReqRoom reqRoom,@SessionAttribute User user){
        String roomid = reqRoom.getId();
        return RespEntity.success(roomComponent.getNextPlayer(roomid,user));
    }

}
