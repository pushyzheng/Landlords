package site.pushy.landlords.core.enums;

/**
 * @author Pushy
 * @since 2019/1/14 17:29
 */
public enum RoomStatusEnum {

    PLAYING("游戏中"),
    PREPARING("准备中");

    private String value;

    RoomStatusEnum(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

}
