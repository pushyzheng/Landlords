package site.pushy.landlords;

import site.pushy.landlords.common.enums.CardNumberEnum;
import site.pushy.landlords.pojo.Card;

import java.util.Collections;
import java.util.List;

/**
 * @author Pushy
 * @since 2018/12/28 21:51
 */
public class Main {

    private static int JOKER_BOMB_NUMBER_SUM =
            CardNumberEnum.BIG_JOKER.getValue() + CardNumberEnum.SMALL_JOKER.getValue();

    private static boolean isEmpty(List<Card> cards) {
        if (cards == null) {
            return true;
        }
        return cards.size() == 0;
    }

    /**
     * 判断是否出的牌是单牌
     */
    public static boolean isSingle(List<Card> cards) {
        if (!isEmpty(cards)) {
            return cards.size() == 1;
        }
        return false;
    }

    /**
     * 判断是否出的牌是对子
     */
    public static boolean isPair(List<Card> cards) {
        if (!isEmpty(cards) && cards.size() == 2) {
            int grade1 = cards.get(0).getGrade();
            int grade2 = cards.get(1).getGrade();
            if (grade1 == grade2) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否出的牌是三张不带牌
     */
    public static boolean isThree(List<Card> cards) {
        if (!isEmpty(cards) && cards.size() == 3) {
            return isAllEqual(cards);
        }
        return false;
    }

    /**
     * 判断是否出的牌是三带一
     */
    public static boolean isThreeWithOne(List<Card> cards) {
        if (!isEmpty(cards) && cards.size() == 4) {
            Collections.sort(cards);  // 对牌进行排序
            if (isAllEqual(cards)) {
                return false;
            } else if (cards.get(0).equals(cards.get(1)) && cards.get(0).equals(cards.get(2))) {
                /* 是3带1，并且被带的牌在牌头 */
                return true;
            } else if (cards.get(3).equals(cards.get(1)) && cards.get(3).equals(cards.get(2))) {
                /* 是3带1，并且被带的牌在牌尾 */
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否出的牌是炸弹
     */
    public static boolean isBomb(List<Card> cards) {
        if (!isEmpty(cards) && cards.size() == 4) {
            return isAllEqual(cards);
        }
        return false;
    }

    /**
     * 判断是否出的牌是王炸
     */
    public static boolean isJokerBomb(List<Card> cards) {
        if (!isEmpty(cards) && cards.size() == 2) {
            int numberSum = 0;
            for (Card card : cards) {
                numberSum += card.getNumber().getValue();
            }
            return numberSum == JOKER_BOMB_NUMBER_SUM;
        }
        return false;
    }

    /**
     * 判断是否出的牌是顺子
     * @return
     */
    public static boolean isStraight(List<Card> cards) {
        return true;
    }

    /**
     * 判断传入的卡牌数组是否全部是相同数字的牌，如4444，返回true
     */
    public static boolean isAllEqual(List<Card> cards) {
        Card first = cards.get(0);
        for (int i = 1; i < cards.size(); i++) {
            if (!first.equals(cards.get(i))) {
                return false;
            }
        }
        return true;
    }

}
