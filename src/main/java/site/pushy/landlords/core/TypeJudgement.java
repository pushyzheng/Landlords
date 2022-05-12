package site.pushy.landlords.core;

import site.pushy.landlords.core.enums.CardGradeEnum;
import site.pushy.landlords.pojo.Card;

import java.util.*;

import static site.pushy.landlords.core.enums.CardGradeEnum.*;

/**
 * @author Pushy
 * @since 2018/12/28 21:51
 */
public class TypeJudgement {

    private static final List<CardGradeEnum> ILLEGAL_GRADES_OF_STRAIGHT =
            Arrays.asList(THIRTEENTH, FOURTEENTH, FIFTEENTH);

    private static boolean isEmpty(List<Card> cards) {
        if (cards == null) return true;
        return cards.size() == 0;
    }

    /**
     * 判断是否出的牌是单牌
     */
    public static boolean isSingle(List<Card> cards) {
        return !isEmpty(cards) && cards.size() == 1;
    }

    /**
     * 判断是否出的牌是对子
     */
    public static boolean isPair(List<Card> cards) {
        if (isEmpty(cards) || cards.size() != 2) {
            return false;
        }
        return isAllGradeEqual(cards);
    }

    /**
     * 判断是否出的牌是三张不带牌
     */
    public static boolean isThree(List<Card> cards) {
        if (isEmpty(cards) || cards.size() != 3) {
            return false;
        }
        return isAllGradeEqual(cards);
    }

    /**
     * 判断是否出的牌是炸弹
     */
    public static boolean isBomb(List<Card> cards) {
        if (isEmpty(cards) || cards.size() != 4) {
            return false;
        }
        return isAllGradeEqual(cards);
    }

    /**
     * 判断是否出的牌是王炸
     */
    public static boolean isJokerBomb(List<Card> cards) {
        if (isEmpty(cards) || cards.size() != 2) {
            return false;
        }
        CardGradeEnum cg1 = cards.get(0).getGrade(), cg2 = cards.get(1).getGrade();
        return (cg1 == FOURTEENTH && cg2 == FIFTEENTH)
                || (cg1 == FIFTEENTH && cg2 == FOURTEENTH);
    }

    /**
     * 判断是否出的牌是三带一
     */
    public static boolean isThreeWithOne(List<Card> cards) {
        if (isEmpty(cards) || cards.size() != 4) {
            return false;
        }
        // 防止该算法将炸弹判定为三带一
        if (isAllGradeEqual(cards)) {
            return false;
        }

        CardUtils.sortCards(cards);
        // 是 3 带 1，并且被带的牌在牌头
        if (cards.get(0).equalsByGrade(cards.get(1)) && cards.get(0).equalsByGrade(cards.get(2))) {
            return true;
        }
        // 是 3 带 1，并且被带的牌在牌尾
        // noinspection RedundantIfStatement
        if (cards.get(3).equalsByGrade(cards.get(1)) && cards.get(3).equalsByGrade(cards.get(2))) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是三带一对
     */
    public static boolean isThreeWithPair(List<Card> cards) {
        if (isEmpty(cards) || cards.size() != 5) {
            return false;
        }
        CardUtils.sortCards(cards);
        if (cards.get(0).equalsByGrade(cards.get(1)) && cards.get(0).equalsByGrade(cards.get(2))) {
            return cards.get(3).equalsByGrade(cards.get(4));
        }
        if (cards.get(2).equalsByGrade(cards.get(3))
                && cards.get(2).equalsByGrade(cards.get(4))
                && cards.get(3).equalsByGrade(cards.get(4))) {
            return cards.get(0).equalsByGrade(cards.get(1));
        }
        return false;
    }

    /**
     * 判断是否出的牌是四带二
     */
    public static boolean isFourWithTwo(List<Card> cards) {
        if (isEmpty(cards) || cards.size() != 6) {
            return false;
        }
        CardUtils.sortCards(cards);

        for (int i = 0; i < 3; i++) {
            int grade1 = cards.get(i).getGradeValue();
            int grade2 = cards.get(i + 1).getGradeValue();
            int grade3 = cards.get(i + 2).getGradeValue();
            int grade4 = cards.get(i + 3).getGradeValue();
            if (grade2 == grade1 && grade3 == grade1 && grade4 == grade1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否出的牌是顺子
     */
    public static boolean isStraight(List<Card> cards) {
        if (isEmpty(cards) || cards.size() < 5) {  // 顺子不能小于5个
            return false;
        }
        CardUtils.sortCards(cards);
        Card last = cards.get(cards.size() - 1);
        if (ILLEGAL_GRADES_OF_STRAIGHT.contains(last.getGrade())) { // 顺子最大的数只能是A
            return false;
        }
        // 判断卡片数组是不是递增的，如果是递增的，说明是顺子
        for (int i = 0; i < cards.size() - 1; i++) {
            // 将每一张牌和它的后一张牌对比，是否相差 1
            if ((cards.get(i).getGradeValue() + 1) != cards.get(i + 1).getGradeValue()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否出的牌是连对
     */
    public static boolean isStraightPair(List<Card> cards) {
        // 连对的牌必须满足大于 6 张牌，而且必须是双数
        if (isEmpty(cards) || cards.size() < 6 || cards.size() % 2 != 0) {
            return false;
        }
        CardUtils.sortCards(cards);
        Card last = cards.get(cards.size() - 1);
        if (ILLEGAL_GRADES_OF_STRAIGHT.contains(last.getGrade())) { // 连对最大的数只能是 A
            return false;
        }
        for (int i = 0; i < cards.size(); i += 2) {
            Card current = cards.get(i);   // 当前牌
            Card next = cards.get(i + 1);  // 下一张牌

            // 判断牌尾的两张牌是否相等
            if (i == cards.size() - 2) {
                if (!current.equalsByGrade(next)) {
                    return false;
                }
                // 判断完是否相等可以跳出循环
                // 因为不需要和下一个连对数（下一张牌的下一张牌）进行比较
                break;
            }
            Card nextNext = cards.get(i + 2);  // 下一张牌的下一张牌
            // 判断是否和下一牌的牌数相同
            if (!current.equalsByGrade(next)) {
                return false;
            }
            // 判断当前的连对数是否和下一个连对数递增1，如果是，则满足连对的出牌规则
            if ((current.getGradeValue() + 1) != nextNext.getGradeValue()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否出的牌是飞机带翅膀
     */
    public static boolean isAircraftWithWing(List<Card> cards) {
        if (isEmpty(cards) || cards.size() < 6) {
            return false;
        }
        CardUtils.sortCards(cards);

        // TODO:
        System.out.println(cards);
        return true;
    }

    /**
     * 判断是否出的牌是飞机
     */
    public static boolean isAircraft(List<Card> cards) {
        if (isEmpty(cards) || cards.size() < 3 || cards.size() % 3 != 0) {
            return false;
        }
        CardUtils.sortCards(cards);

        for (int i = 0; i < cards.size(); i += 3) {
            if (i == cards.size() - 3) {
                // 比较最后一个飞机的三张牌是否相等
                return isAllGradeEqual(cards, i, cards.size() - 1);
            }
            // 如果当前这张牌不和下边的两张牌相同，则不符合飞机的出牌规则
            if (!isAllGradeEqual(cards, i, i + 2)) {
                return false;
            }
            // 判断当前飞机等级是否和下一个飞机的等级递增 1
            // 如果是，则符合飞机的出牌规则
            if ((cards.get(i).getGradeValue() + 1) != cards.get(i + 3).getGradeValue()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断传入的卡牌数组是否全部是相同等级的牌，如 4444，返回true
     */
    public static boolean isAllGradeEqual(List<Card> cards) {
        return isAllGradeEqual(cards, 0, cards.size() - 1);
    }

    public static boolean isAllGradeEqual(List<Card> cards, int start, int end) {
        if (start > end || end >= cards.size()) {
            throw new IllegalArgumentException("start or end is illegal");
        }
        if (start == end) {
            return true;
        }
        Card first = cards.get(start);
        for (int i = start + 1; i < end + 1; i++) {
            if (!first.equalsByGrade(cards.get(i))) {
                return false;
            }
        }
        return true;
    }
}
