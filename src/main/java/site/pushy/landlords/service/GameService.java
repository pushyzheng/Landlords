package site.pushy.landlords.service;

import site.pushy.landlords.pojo.Card;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.Player;
import site.pushy.landlords.pojo.Room;
import site.pushy.landlords.pojo.RoundResult;

import java.util.List;

/**
 * @author Pushy and Fuxing
 * @since 2019/1/9 21:19
 */
public interface GameService {

    /**
     * 玩家准备游戏
     * @return 是否开局，当房间的内的所有玩家都准备，并且人数已满3人，即为开局
     */
    boolean readyGame(User user);

    /**
     * 取消准备
     */
    void unReadyGame(User curUser);

    /**
     * 通知所有房间内的玩家开始游戏，并更新房间的状态
     */
    void startGame(String roomId);

    /**
     * 叫牌，并分配该玩家该地主身份，并将三张地主加入到该玩家的牌中
     */
    void want(User user, int score);

    /**
     * 轮到此人叫牌，选择不叫地主，将叫地主消息传递给下一家
     */
    void noWant(User user);

    /**
     * 出牌
     */
    RoundResult playCard(User user, List<Card> cardList);

    /**
     * 要不起
     */
    void pass(User user);

}
