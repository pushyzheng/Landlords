package site.pushy.landlords.controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import site.pushy.landlords.common.exception.BadRequestException;
import site.pushy.landlords.core.component.RoomComponent;
import site.pushy.landlords.pojo.ApiResponse;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.DTO.CreateRoomDTO;
import site.pushy.landlords.pojo.DTO.RoomDTO;
import site.pushy.landlords.pojo.DTO.RoomListOutputDTO;
import site.pushy.landlords.pojo.DTO.RoomOutDTO;
import site.pushy.landlords.pojo.Room;
import site.pushy.landlords.service.RoomService;

import javax.annotation.Resource;
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

    @Resource
    private RoomComponent roomComponent;

    @Resource
    private RoomService roomService;

    /**
     * 获取所有房间列表
     */
    @GetMapping("")
    public ApiResponse<?> listRoom() {
        List<Room> roomList = roomComponent.getRooms();
        if (roomList.size() != 0) {
            List<RoomListOutputDTO> dtoList = roomList.stream()
                    .map(RoomListOutputDTO::new)
                    .collect(Collectors.toList());
            return ApiResponse.success(dtoList);
        }
        return ApiResponse.success(roomList);
    }

    /**
     * 通过房间号id查看某房间的所有信息，该玩家必须在该房间内
     */
    @GetMapping("/{id}")
    public ApiResponse<RoomOutDTO> getRoomById(@PathVariable String id,
                                               @SessionAttribute User curUser) {
        return ApiResponse.success(roomService.getRoomById(curUser, id));
    }

    /**
     * 创建房间
     */
    @PostMapping("")
    public ApiResponse<Room> createRoom(@SessionAttribute User curUser,
                                        @Valid @RequestBody CreateRoomDTO body) {
        if (!StringUtils.hasLength(body.getTitle())) {
            throw new BadRequestException("房间名称不能为空");
        }
        return ApiResponse.success(roomService.createRoom(curUser, body.getTitle(), body.getPassword()));
    }

    /**
     * 加入房间
     */
    @PostMapping("/join")
    public ApiResponse<String> joinRoom(@Valid @RequestBody RoomDTO roomDTO,
                                        @SessionAttribute User curUser) {
        return ApiResponse.success(roomService.joinRoom(curUser, roomDTO));
    }

    /**
     * 退出房间
     */
    @PostMapping("/exit")
    public ApiResponse<Boolean> exitRoom(@SessionAttribute User curUser) {
        return ApiResponse.success(roomService.exitRoom(curUser));
    }
}
