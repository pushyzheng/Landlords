package site.pushy.landlords.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.pushy.landlords.core.enums.TypeEnum;
import site.pushy.landlords.pojo.Card;

import java.util.Collections;
import java.util.List;

/**
 * @author Pushy
 * @since 2018/12/29 15:09
 */
public class GradeComparison {

    private static final Logger logger = LoggerFactory.getLogger(GradeComparison.class.getSimpleName());

    /**
     * 判断是否可以出牌，也就是当前玩家的牌是否比上家的大
     *
     * @param myCards   当前玩家的牌
     * @param prevCards 上家的牌
     */
    public static boolean canPlayCards(List<Card> myCards, TypeEnum myType,
                                       List<Card> prevCards, TypeEnum prevType) {
        if (myCards == null || prevCards == null) {
            return false;
        }
        if (myType == null || prevType == null) {
            return false;
        }
        /* 判断上家出的牌或者我出的牌是否是王炸 */
        if (prevType == TypeEnum.JOKER_BOMB) {
            return false;
        } else if (myType == TypeEnum.JOKER_BOMB) {
            return true;
        }
        /* 如果上家不是炸弹，而我出炸弹，则可以出牌 */
        if (prevType != TypeEnum.BOMB && myType == TypeEnum.BOMB) {
            return true;
        }

        Collections.sort(myCards);
        Collections.sort(prevCards);

        int mySize = myCards.size();
        int prevSize = prevCards.size();
        Card myCard = myCards.get(0);
        Card prevCard = prevCards.get(0);

        /* 排除玩家出牌和出牌类型不一致的情况 */
        if (prevType != myType) {
            return false;
        }
        /* 3带1，只需要比较第二张牌的等级即可 */
        if (prevType == TypeEnum.THREE_WITH_ONE) {
            myCard = myCards.get(1);
            prevCard = prevCards.get(1);
            return CardUtils.compareGradeTo(myCard, prevCard);
        }
        /* 3带一对，只需要比较第三张牌即可 */
        else if (prevType == TypeEnum.THREE_WITH_PAIR) {
            myCard = myCards.get(2);
            prevCard = prevCards.get(2);
            return CardUtils.compareGradeTo(myCard, prevCard);
        }
        /* 4带2，只需要比较第三张牌大小的等级 */
        else if (prevType == TypeEnum.FOUR_WITH_TWO) {
            myCard = myCards.get(2);
            prevCard = prevCards.get(2);
            return CardUtils.compareGradeTo(myCard, prevCard);
        }
        /* 顺子、连对一样，只需要比较最大的一张牌的大小 */
        else if (prevType == TypeEnum.STRAIGHT || prevType == TypeEnum.STRAIGHT_PAIR) {
            if (mySize != prevSize) {  // 出的顺子牌数不同，无法出牌
                return false;
            }
            myCard = myCards.get(mySize - 1);
            prevCard = prevCards.get(prevSize - 1);
            return CardUtils.compareGradeTo(myCard, prevCard);
        }
        /* 飞机 */
        else if (prevType == TypeEnum.AIRCRAFT) {
            if (mySize == prevSize) {
                return false;
            } else {
                // Todo 飞机判断存在问题
                myCard = myCards.get(5);
                prevCard = prevCards.get(5);
                return CardUtils.compareGradeTo(myCard, prevCard);
            }
        }
        /* 单张、对子、三张、炸弹等情况，都是只需要判断第一张牌大小即可 */
        else {
            return CardUtils.compareGradeTo(myCard, prevCard);
        }
    }

    /**
     * 判断当前玩家手中是否有牌可以管住上家出的牌
     *
     * @param myCards   当前玩家手中所有的牌
     * @param prevCards 上家出的牌
     * @param prevType  上家出的牌的类型
     */
    public static boolean hasHighGradeCards(List<Card> myCards,
                                            List<Card> prevCards, TypeEnum prevType) {
        if (myCards == null || prevCards == null) {
            return false;
        }
        if (prevType == null) {
            logger.error("上家出的牌不合法，无法出牌");
            return false;
        }

        // Todo 实现 => 判断当前玩家手中是否有牌可以管住上家出的牌

        Collections.sort(myCards);
        Collections.sort(prevCards);
        return false;
    }
}
