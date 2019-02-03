package site.pushy.landlords.core.enums;

/**
 * @author Pushy
 * @since 2019/1/24 12:25
 */
public enum WsMessageTypeEnum {

    READY_GAME("玩家准备"),
    START_GAME("开始游戏"),

    PLAYER_JOIN("有玩家加入"),
    PLAYER_EXIT("有玩家退出"),

    BID("叫牌"),
    BID_END("叫牌结束"),

    PLAY_CARD("有玩家出牌"),
    PLEASE_PLAY_CARD("请出牌");

    private String value;

    WsMessageTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
