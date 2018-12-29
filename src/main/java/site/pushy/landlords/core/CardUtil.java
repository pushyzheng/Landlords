package site.pushy.landlords.core;

import site.pushy.landlords.pojo.Card;

/**
 * @author Pushy
 * @since 2018/12/29 15:52
 */
public class CardUtil {

    /**
     * 比较两张牌的等级值，返回1则代表card > other，-1 代表 card < other，0代表等级相同
     */
    public static int compareGrade(Card card, Card other) {
        if (card.getGrade() == null || other.getGrade() == null) {
            return 0;
        }
        return Integer.compare(card.getGradeValue(), other.getGradeValue());
    }

    /**
     * 判断一张牌的等级是否大于另一张
     */
    public static boolean compareGradeTo(Card card, Card other) {
        return compareGrade(card, other) == 1;
    }

}
