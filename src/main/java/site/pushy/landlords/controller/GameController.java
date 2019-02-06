package site.pushy.landlords.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.pushy.landlords.common.util.RespEntity;
import site.pushy.landlords.core.component.RoomComponent;
import site.pushy.landlords.pojo.Card;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.DTO.BidDTO;
import site.pushy.landlords.pojo.DTO.ReadyGameDTO;
import site.pushy.landlords.pojo.DTO.RoomDTO;
import site.pushy.landlords.pojo.Room;
import site.pushy.landlords.service.GameService;
import site.pushy.landlords.service.PlayerService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pushy
 * @since 2019/1/9 21:16
 */
@RestController
@RequestMapping(value = "/games", produces = "application/json")
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerService playerService;

    /**
     * 准备
     */
    @PostMapping("/ready")
    public String readyGame(@SessionAttribute User curUser,
                            @Valid @RequestBody ReadyGameDTO readyGameDTO) {
        boolean start = gameService.readyGame(curUser);
        if (start) {
            gameService.startGame(readyGameDTO.getRoomId());
        }
        return RespEntity.success(start);
    }

    /**
     * 玩家获取自己的牌
     */
    @GetMapping("/cards")
    public String getCardsByPlayerId(@SessionAttribute User curUser) {
        List<Card> cards = playerService.getPlayerCards(curUser);
        return RespEntity.success(cards);
    }

    /**
     * 叫牌
     */
    @PostMapping("/bid")
    public String bid(@Valid @RequestBody BidDTO bidDTO,
                      @SessionAttribute User curUser) {
        // 轮到此人叫牌，选择不叫地主，将叫地主消息传递给下一家
        if (bidDTO.isWant()) {
            gameService.want(curUser, bidDTO.getScore());
            return RespEntity.success("已叫地主并分配身份");
        }
        // 叫牌，并分配身份
        else {
            gameService.noWant(curUser);
            return RespEntity.success("已选择不叫地主，并传递给下家");
        }
    }

    /**
     * 出牌
     */
    @PostMapping("/play")
    public String outCard(@SessionAttribute User curUser,
                          @RequestBody List<Card> cardList){
        gameService.playCard(curUser,cardList);
        return RespEntity.success("success");
    }

    @PostMapping("/pass")
    public String pass(@SessionAttribute User curUser) {
        gameService.pass(curUser);
        return RespEntity.success("success");
    }

}
