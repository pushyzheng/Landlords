package site.pushy.landlords.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.pushy.landlords.common.util.RespEntity;
import site.pushy.landlords.core.component.RoomComponent;
import site.pushy.landlords.pojo.Card;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.DTO.ReadyGameDTO;
import site.pushy.landlords.pojo.DTO.RoomDTO;
import site.pushy.landlords.pojo.Room;
import site.pushy.landlords.service.GameService;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Pushy
 * @since 2019/1/9 21:16
 */
@RestController
@RequestMapping("/games")
public class GameController {
    @Autowired
    private RoomComponent roomComponent;

    @Autowired
    private GameService gameService;

    @PostMapping("/ready")
    public String readyGame(@SessionAttribute User curUser,
                            @Valid @RequestBody ReadyGameDTO readyGameDTO) {
        boolean start = gameService.readyGame(curUser, readyGameDTO);
        if (start) {
            gameService.startGame(readyGameDTO.getRoomId());
        }
        return RespEntity.success(start);
    }

    /**
     * 随机分配一位用户叫牌
     * @param roomDTO
     * @return
     */
    @PostMapping("/order")
    public String wantCardOrder(@Valid @RequestBody RoomDTO roomDTO){
        String roomId = roomDTO.getId();
        gameService.wantCardOrder(roomId);
        return RespEntity.success("已随机分配一位用户叫牌");
    }

    /**
     * 轮到此人叫牌,选择不叫地主.将叫地主消息传递给下一家
     * @param roomDTO
     * @param curUser
     * @return
     */
    @PostMapping("/nowant")
    public String noWantCard(@Valid @RequestBody RoomDTO roomDTO,
                             @SessionAttribute User curUser){
        String roomId = roomDTO.getId();
        Room room = roomComponent.getRoom(roomDTO.getId());
        gameService.noWantCard(roomId,curUser);
        return RespEntity.success("已选择不叫地主,并传递给下家");
    }

    /**
     * 叫牌,并分配身份
     * @param roomDTO
     * @param curUser
     * @return
     */
    @PostMapping("/want")
    public String wantCard(@Valid @RequestBody RoomDTO roomDTO,
                           @SessionAttribute User curUser){
        String roomId = roomDTO.getId();
        Room room = roomComponent.getRoom(roomDTO.getId());
        gameService.wantCard(roomId,curUser);
        return RespEntity.success("已叫地主并分配身份");
    }

    /**
     * 出牌
     * @param roomDTO
     * @param curUser
     * @param cardList
     * @return
     */
    @PostMapping("/out")
    public String outCard(@Valid @RequestBody RoomDTO roomDTO,
                          @SessionAttribute User curUser,@RequestBody List<Card> cardList){
        String roomId = roomDTO.getId();
        Room room = roomComponent.getRoom(roomDTO.getId());
        gameService.outCard(roomId,curUser,cardList);
        return RespEntity.success("已出牌"+cardList);
    }

}
