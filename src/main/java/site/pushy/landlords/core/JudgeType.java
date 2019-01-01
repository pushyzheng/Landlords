package site.pushy.landlords.core;

import site.pushy.landlords.core.enums.CardNumberEnum;
import site.pushy.landlords.pojo.Card;

import java.util.Collections;
import java.util.List;

/**
 * @author Pushy
 * @since 2018/12/28 21:51
 */
public class JudgeType {

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
            int value1 = cards.get(0).getNumberValue();
            int value2 = cards.get(1).getNumberValue();
            if (value1 == value2) {
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
                numberSum += card.getNumberValue();
            }
            return numberSum == JOKER_BOMB_NUMBER_SUM;
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
        Collections.sort(cards);

        /* 判断卡片数组是不是递增的，如果是递增的，说明是顺子 */
        for (int i = 0; i < cards.size(); i++) {
            if (i == cards.size() - 1) {
                /* 牌尾那张牌不需要再进行比较，直接跳出循环 */
                break;
            }
            /* 将每一张牌和它的后一张牌对比，是否相差1 */
            Card current = cards.get(i);
            Card next = cards.get(i + 1);
            if ((current.getNumberValue() + 1) != next.getNumberValue()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否出的牌是连对
     */
    public static boolean isStraightPair(List<Card> cards) {
        /* 连对的牌必须满足大于6张牌，而且必须是双数 */
        if (isEmpty(cards) || cards.size() < 6 || cards.size() % 2 != 0) {
            return false;
        }
        Collections.sort(cards);

        for (int i = 0; i < cards.size(); i += 2) {
            Card current = cards.get(i);   // 当前牌
            Card next = cards.get(i + 1);  // 下一张牌

            /* 判断牌尾的两张牌是否相等 */
            if (i == cards.size() - 2) {
                if (!current.equals(next)) {
                    return false;
                }
                /* 判断完是否相等可以跳出循环，因为不需要和下一个连对数（下一张牌的下一张牌）进行比较 */
                break;
            }

            Card nextNext = cards.get(i + 2);  // 下一张牌的下一张牌
            /* 判断是否和下一牌的牌数相同 */
            if (!current.equals(next)) {
                return false;
            }
            /* 判断当前的连对数是否和下一个连对数递增1，如果是，则满足连对的出牌规则 */
            if ((current.getNumberValue() + 1) != nextNext.getNumberValue()) {
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
        Collections.sort(cards);

        System.out.println(cards);
        return true;
    }

    /**
     * 判断是否出的牌是飞机
     */
    public static boolean isAircraft(List<Card> cards) {
        if (isEmpty(cards) || cards.size() % 3 != 0) {
            return false;
        }
        Collections.sort(cards);

        for (int i = 0; i < cards.size(); i += 3) {

            if (i == cards.size() - 3) {
                /* 比较最后一个飞机的三张牌是否相等 */
                return isAllGradeEqual(cards.subList(cards.size() - 3, cards.size()));
            }
            /* 如果当前这张牌不和下边的两张牌相同，则不符合飞机的出牌规则 */
            if (!isAllGradeEqual(cards.subList(i, i + 2))) {
                return false;
            }
            /* 判断当前飞机等级是否和下一个飞机的等级递增1，如果是，则符合飞机的出牌规则 */
            if ((cards.get(i).getGradeValue() + 1) != cards.get(i + 3).getGradeValue()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否出的牌是四带二
     */
    public static boolean isFourWithTwo(List<Card> cards) {
        if (isEmpty(cards) || cards.size() != 6) {
            return false;
        }
        Collections.sort(cards);

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
     * 判断传入的卡牌数组是否全部是相同数字的牌，如4444，返回true
     */
    private static boolean isAllEqual(List<Card> cards) {
        Card first = cards.get(0);
        for (int i = 1; i < cards.size(); i++) {
            if (!first.equals(cards.get(i))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isAllGradeEqual(List<Card> cards) {
        Card first = cards.get(0);
        for (int i = 1; i < cards.size(); i++) {
            if (first.getGradeValue() != cards.get(i).getGradeValue()) {
                return false;
            }
        }
        return true;
    }

}
