package site.pushy.landlords.common.enums;

/**
 * @author Pushy
 * @since 2018/12/28 21:22
 */
public enum CardTypeEnum {

    BLACK_PEACH("黑桃"),
    RED_PEACH("红桃"),
    PLUM_FLOWER("梅花"),
    SQUARE("方块"),

    SMALL_JOKER("小王"),
    BIG_JOKER("大王");

    private String name;

    CardTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
