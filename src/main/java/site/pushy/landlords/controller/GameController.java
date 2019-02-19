package site.pushy.landlords.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.pushy.landlords.common.exception.ForbiddenException;
import site.pushy.landlords.common.util.RespEntity;
import site.pushy.landlords.core.CardUtil;
import site.pushy.landlords.core.enums.TypeEnum;
import site.pushy.landlords.pojo.Card;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.DTO.BidDTO;
import site.pushy.landlords.pojo.DTO.ReadyGameDTO;
import site.pushy.landlords.pojo.RoundResult;
import site.pushy.landlords.service.AchievementService;
import site.pushy.landlords.service.GameService;
import site.pushy.landlords.service.PlayerService;

import javax.validation.Valid;
import java.util.List;

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

    @Autowired
    private AchievementService achievementService;

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
     * 取消准备
     */
    @PostMapping("/unReady")
    public String unReadyGame(@SessionAttribute User curUser) {
        gameService.unReadyGame(curUser);
        return RespEntity.success("success");
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
                          @RequestBody List<Card> cardList) {
        validRound(curUser);
        RoundResult result = gameService.playCard(curUser, cardList);
        if (result != null) {
            achievementService.countScore(curUser, result);
        }
        return RespEntity.success(result == null ? "success" : result);
    }

    @PostMapping("/pass")
    public String pass(@SessionAttribute User curUser) {
        validRound(curUser);
        gameService.pass(curUser);
        return RespEntity.success("success");
    }

    private void validRound(User curUser) {
        boolean result = playerService.isPlayerRound(curUser);
        if (!result) {
            throw new ForbiddenException("当前不是该玩家出牌回合");
        }
    }

}
