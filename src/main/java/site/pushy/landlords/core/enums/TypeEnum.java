package site.pushy.landlords.core.enums;

/**
 * @author Pushy
 * @since 2018/12/29 15:20
 */
public enum TypeEnum {

    SINGLE("单张"),
    PAIR("对子"),
    THREE("三张"),
    THREE_WITH_ONE("三带一"),
    THREE_WITH_PAIR("三带一对"),

    BOMB("炸弹"),
    JOKER_BOMB("王炸"),

    STRAIGHT("顺子"),
    STRAIGHT_PAIR("连对"),

    AIRCRAFT_WITH_WINGS("飞机带翅膀"),
    AIRCRAFT("飞机"),

    FOUR_WITH_TWO("四带二");

    private String name;

    TypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
