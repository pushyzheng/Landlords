package site.pushy.landlords.core;

import site.pushy.landlords.core.enums.TypeEnum;
import site.pushy.landlords.pojo.Card;

import java.util.List;

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

    public static boolean compareGradeTo(int grade, int other) {
        return grade > other;
    }

    /**
     * 判断几张牌的类型
     *
     * @see TypeEnum
     */
    public static TypeEnum getCardType(List<Card> cards) {
        TypeEnum type = null;
        if (cards != null && cards.size() != 0) {
            if (TypeJudge.isSingle(cards)) {
                type = TypeEnum.SINGLE;
            } else if (TypeJudge.isPair(cards)) {
                type = TypeEnum.PAIR;
            } else if (TypeJudge.isThree(cards)) {
                type = TypeEnum.THREE;
            } else if (TypeJudge.isThreeWithOne(cards)) {
                type = TypeEnum.THREE_WITH_ONE;
            } else if (TypeJudge.isStraight(cards)) {
                type = TypeEnum.STRAIGHT;
            } else if (TypeJudge.isStraightPair(cards)) {
                type = TypeEnum.STRAIGHT_PAIR;
            } else if (TypeJudge.isFourWithTwo(cards)) {
                type = TypeEnum.FOUR_WITH_TWO;
            } else if (TypeJudge.isBomb(cards)) {
                type = TypeEnum.BOMB;
            } else if (TypeJudge.isJokerBomb(cards)) {
                type = TypeEnum.JOKER_BOMB;
            } else if (TypeJudge.isAircraft(cards)) {
                type = TypeEnum.AIRCRAFT;
            } else if (TypeJudge.isAircraftWithWing(cards)) {
                type = TypeEnum.AIRCRAFT_WITH_WINGS;
            }
        }
        return type;
    }

}
