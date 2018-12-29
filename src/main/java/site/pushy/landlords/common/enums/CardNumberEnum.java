package site.pushy.landlords.common.enums;

/**
 * 牌的数值枚举值
 *
 * @author Pushy
 * @since 2018/12/28 21:23
 */
public enum CardNumberEnum {

    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),

    JACK(11),         // J
    LADY(12),         // Q
    KING(13),         // K

    SMALL_JOKER(14),  // 小王
    BIG_JOKER(15);    // 大王

    private int value;

    CardNumberEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
