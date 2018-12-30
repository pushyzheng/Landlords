package site.pushy.landlords.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.pushy.landlords.core.enums.CardGradeEnum;
import site.pushy.landlords.core.enums.CardNumberEnum;
import site.pushy.landlords.core.enums.CardTypeEnum;

/**
 * @author Pushy
 * @since 2018/12/30 23:00
 */
public class ConstructCard {

    private static final Logger logger = LoggerFactory.getLogger(ConstructCard.class.getSimpleName());

    /**
     * 通过id获取单张牌的类型
     * @see CardTypeEnum
     */
    public static CardTypeEnum getTypeById(int id) {
        CardTypeEnum type;

        if (id >= 1 && id <= 13) {
            type = CardTypeEnum.DIAMOND;
        } else if (id >= 14 && id <= 26) {
            type = CardTypeEnum.CLUB;
        } else if (id >= 27 && id <= 39) {
            type = CardTypeEnum.HEART;
        } else if (id >= 40 && id <= 52) {
            type = CardTypeEnum.SPADE;
        } else if (id == 53) {
            type = CardTypeEnum.SMALL_JOKER;
        } else if (id == 54) {
            type = CardTypeEnum.BIG_JOKER;
        } else {
            type = null;
        }
        return type;
    }

    /**
     * 通过牌的id获取单张牌的数值
     * @see CardNumberEnum
     */
    public static CardNumberEnum getNumberById(int id) {
        if (id < 1 || id > 54) {
            logger.error("输入的牌的id错误 => " + id);
            throw new RuntimeException("输入的牌的id错误");
        }

        CardNumberEnum number;
        if (id >= 1 && id <= 52) {
            number = convertNumToNumber(id % 13);
        } else if (id == 53) {  // 小王
            number = CardNumberEnum.SMALL_JOKER;
        } else if (id == 54) {  // 大王
            number = CardNumberEnum.BIG_JOKER;
        } else {
            number = null;
        }
        return number;
    }

    public static CardGradeEnum getGradeById(int id) {
        if (id < 1 || id > 54) {
            logger.error("输入的牌的id错误");
            throw  new RuntimeException("输入的牌的id错误");
        }
        CardGradeEnum grade;
        if (id == 53) {  // 小王
            grade = CardGradeEnum.FOURTEENTH;
        } else if (id == 54) {  // 大王
            grade = CardGradeEnum.FIFTEENTH;
        }
        /* 3 ~ K、A、2的情况，通过取余数后转换为对应的 CardNumberEnum */
        else {
            int modResult = id % 13;
            grade = convertNumToGrade(modResult);
        }
        return grade;
    }

    /**
     * 将数字转换为对应的数值枚举值
     * @see CardNumberEnum
     */
    private static CardNumberEnum convertNumToNumber(int num) {
        switch (num) {
            case 0:
                return CardNumberEnum.ONE;
            case 1:
                return CardNumberEnum.TWO;
            case 2:
                return CardNumberEnum.THREE;
            case 3:
                return CardNumberEnum.FOUR;
            case 4:
                return CardNumberEnum.FIVE;
            case 5:
                return CardNumberEnum.SIX;
            case 6:
                return CardNumberEnum.SEVEN;
            case 7:
                return CardNumberEnum.EIGHT;
            case 8:
                return CardNumberEnum.NINE;
            case 9:
                return CardNumberEnum.TEN;
            case 10:
                return CardNumberEnum.JACK;
            case 11:
                return CardNumberEnum.LADY;
            case 12:
                return CardNumberEnum.KING;
        }
        return null;
    }

    /**
     * 将数字转换为对应的等级枚举值
     * @see CardGradeEnum
     */
    private static CardGradeEnum convertNumToGrade(int num) {
        switch (num) {
            case 0:  // A
                return CardGradeEnum.TWELFTH;
            case 1:  // 2
                return CardGradeEnum.THIRTEENTH;
            case 2:  // 3
                return CardGradeEnum.FIRST;
            case 3:
                return CardGradeEnum.SECOND;
            case 4:
                return CardGradeEnum.THIRD;
            case 5:
                return CardGradeEnum.FOURTH;
            case 6:
                return CardGradeEnum.FIFTH;
            case 7:
                return CardGradeEnum.SIXTH;
            case 8:
                return CardGradeEnum.SEVENTH;
            case 9:
                return CardGradeEnum.EIGHTH;
            case 10:  // J
                return CardGradeEnum.NINTH;
            case 11:  // Q
                return CardGradeEnum.TENTH;
            case 12:   // K
                return CardGradeEnum.ELEVENTH;
        }
        return null;
    }


}
