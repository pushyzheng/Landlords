package site.pushy.landlords.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import site.pushy.landlords.pojo.ApiResponse;
import site.pushy.landlords.pojo.Card;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.service.PlayerService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Pushy
 * @since 2019/2/10 21:14
 */
@RestController
@RequestMapping(value = "/player", produces = "application/json")
public class PlayerController {

    @Resource
    private PlayerService playerService;

    /**
     * 玩家获取自己的牌
     */
    @GetMapping("/cards")
    public ApiResponse<List<Card>> getCardsByPlayerId(@SessionAttribute User curUser) {
        List<Card> cards = playerService.getPlayerCards(curUser);
        return ApiResponse.success(cards);
    }

    /**
     * 判断当前是否是某玩家的出牌回合
     */
    @GetMapping("/round")
    public ApiResponse<Boolean> isPlayerRound(@SessionAttribute User curUser) {
        return ApiResponse.success(playerService.isPlayerRound(curUser));
    }

    @GetMapping("/ready")
    public ApiResponse<Boolean> isPlayerReady(@SessionAttribute User curUser) {
        return ApiResponse.success(playerService.isPlayerReady(curUser));
    }

    @GetMapping("/pass")
    public ApiResponse<Boolean> canPass(@SessionAttribute User curUser) {
        return ApiResponse.success(playerService.canPass(curUser));
    }

    @GetMapping("/bidding")
    public ApiResponse<Boolean> bid(@SessionAttribute User curUser) {
        return ApiResponse.success(playerService.canBid(curUser));
    }
}
