package site.pushy.landlords.service;

import site.pushy.landlords.pojo.Card;
import site.pushy.landlords.pojo.DO.User;

import java.util.List;

/**
 * @author Pushy and Fuxing
 * @since 2019/1/9 21:19
 */
public interface GameService {

    /**
     * 准备游戏
     */
    boolean readyGame(User user);

    /**
     * 开始游戏
     */
    void startGame(String roomId);

    /**
     * 叫牌，并分配该玩家该地主身份，并将三张地主加入到该玩家的牌中
     */
    void want(User user);

    /**
     * 轮到此人叫牌，选择不叫地主，将叫地主消息传递给下一家
     */
    void noWant(User user);

    /**
     * 出牌
     */
    void playCard(User user, List<Card> cardList);

    /**
     * 要不起
     */
    void pass(User user);

}
