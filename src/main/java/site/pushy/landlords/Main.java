package site.pushy.landlords;

import site.pushy.landlords.pojo.Card;

import java.util.List;

/**
 * @author Pushy
 * @since 2018/12/28 21:51
 */
public class Main {

    private static boolean isEmpty(List<Card> cards) {
        if (cards == null) {
            return true;
        }
        return cards.size() == 0;
    }

    /**
     * 是否出的是单牌
     */
    public static boolean isSingle(List<Card> cards) {
        if (!isEmpty(cards)) {
            return cards.size() == 1;
        }
        return false;
    }

    /**
     * 是否出的是对子
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
     * 是否出的是三带一
     */
    public static boolean isThreeWithOne(List<Card> cards) {
        if (!isEmpty(cards) && cards.size() == 4) {
            return true;
        }
        return false;
    }

}
