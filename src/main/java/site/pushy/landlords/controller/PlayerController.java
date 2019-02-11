package site.pushy.landlords.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import site.pushy.landlords.common.util.RespEntity;
import site.pushy.landlords.pojo.Card;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.service.GameService;
import site.pushy.landlords.service.PlayerService;

import java.util.List;

/**
 * @author Pushy
 * @since 2019/2/10 21:14
 */
@RestController
@RequestMapping(value = "/player", produces = "application/json")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    /**
     * 玩家获取自己的牌
     */
    @GetMapping("/cards")
    public String getCardsByPlayerId(@SessionAttribute User curUser) {
        List<Card> cards = playerService.getPlayerCards(curUser);
        return RespEntity.success(cards);
    }

    /**
     * 判断当前是否是某玩家的出牌回合
     */
    @GetMapping("/round")
    public String isPlayerRound(@SessionAttribute User curUser) {
        return RespEntity.success(playerService.isPlayerRound(curUser));
    }

    @GetMapping("/ready")
    public String isPlayerReady(@SessionAttribute User curUser) {
        return RespEntity.success(playerService.isPlayerReady(curUser));
    }

    @GetMapping("/pass")
    public String canPass(@SessionAttribute User curUser) {
        return RespEntity.success(playerService.canPass(curUser));
    }
}