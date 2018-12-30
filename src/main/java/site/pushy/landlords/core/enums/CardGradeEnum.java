package site.pushy.landlords.core.enums;

/**
 * 牌的等级枚举值
 *
 * @author Pushy
 * @since 2018/12/29 1:04
 */
public enum CardGradeEnum {

    // 3 ~ K
    FIRST(1),      // 3
    SECOND(2),     // 4
    THIRD(3),      // 5
    FOURTH(4),     // 6
    FIFTH(5),      // 7
    SIXTH(6),      // 8
    SEVENTH(7),    // 9
    EIGHTH(8),     // 10
    NINTH(9),      // J
    TENTH(10),     // Q
    ELEVENTH(11),  // K

    // A 和 2
    TWELFTH(12),
    THIRTEENTH(13),

    // 大小王
    FOURTEENTH(14),
    FIFTEENTH(15);

    private int value;

    CardGradeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }


}
