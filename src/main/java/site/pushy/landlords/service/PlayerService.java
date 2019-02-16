package site.pushy.landlords.service;

import site.pushy.landlords.pojo.Card;
import site.pushy.landlords.pojo.DO.User;

import java.util.List;

/**
 * @author Pushy
 * @since 2019/2/1 18:20
 */
public interface PlayerService {

    /**
     * 获取某玩家当前手中的牌
     */
    List<Card> getPlayerCards(User curUser);

    /**
     * 判断当前是否是某玩家的出牌回合
     */
    boolean isPlayerRound(User curUser);

    /**
     * 判断当前用户是否准备
     */
    boolean isPlayerReady(User curUser);

    /**
     * 判断玩家是否可以不出
     */
    boolean canPass(User curUser);

}
