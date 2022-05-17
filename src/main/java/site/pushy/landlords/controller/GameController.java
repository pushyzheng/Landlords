package site.pushy.landlords.controller;

import org.springframework.web.bind.annotation.*;
import site.pushy.landlords.common.exception.ForbiddenException;
import site.pushy.landlords.pojo.ApiResponse;
import site.pushy.landlords.pojo.Card;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.DTO.BidDTO;
import site.pushy.landlords.pojo.RoundResult;
import site.pushy.landlords.service.AchievementService;
import site.pushy.landlords.service.GameService;
import site.pushy.landlords.service.PlayerService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Pushy
 * @since 2019/1/9 21:16
 */
@RestController
@RequestMapping(value = "/games", produces = "application/json")
public class GameController {

    @Resource
    private GameService gameService;

    @Resource
    private PlayerService playerService;

    @Resource
    private AchievementService achievementService;

    /**
     * 准备
     */
    @PostMapping("/ready")
    public ApiResponse<Boolean> readyGame(@SessionAttribute User curUser) {
        return ApiResponse.success(gameService.readyGame(curUser));
    }

    /**
     * 取消准备
     */
    @PostMapping("/unReady")
    public ApiResponse<String> unReadyGame(@SessionAttribute User curUser) {
        gameService.unReadyGame(curUser);
        return ApiResponse.success("success");
    }

    /**
     * 叫牌
     */
    @PostMapping("/bid")
    public ApiResponse<String> bid(@Valid @RequestBody BidDTO bidDTO,
                      @SessionAttribute User curUser) {
        // 叫牌，并分配身份
        if (bidDTO.isWant()) {
            gameService.want(curUser, bidDTO.getScore());
            return ApiResponse.success("已叫地主并分配身份");
        }
        // 轮到此人叫牌，选择不叫地主，将叫地主消息传递给下一家
        else {
            gameService.noWant(curUser);
            return ApiResponse.success("已选择不叫地主，并传递给下家");
        }
    }

    /**
     * 出牌
     */
    @PostMapping("/play")
    public ApiResponse<?> outCard(@SessionAttribute User curUser,
                          @RequestBody List<Card> cardList) {
        validRound(curUser);
        RoundResult result = gameService.playCard(curUser, cardList);
        if (result != null) {
            achievementService.countScore(curUser, result);
        }
        return ApiResponse.success(result == null ? "success" : result);
    }

    @PostMapping("/pass")
    public ApiResponse<String> pass(@SessionAttribute User curUser) {
        validRound(curUser);
        gameService.pass(curUser);
        return ApiResponse.success("success");
    }

    private void validRound(User curUser) {
        boolean result = playerService.isPlayerRound(curUser);
        if (!result) {
            throw new ForbiddenException("当前不是该玩家出牌回合");
        }
    }
}
