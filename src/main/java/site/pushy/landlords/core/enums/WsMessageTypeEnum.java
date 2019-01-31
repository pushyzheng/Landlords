package site.pushy.landlords.core.enums;

/**
 * @author Pushy
 * @since 2019/1/24 12:25
 */
public enum WsMessageTypeEnum {

    READY_GAME("玩家准备"),
    START_GAME("开始游戏"),

    PLAYER_JOIN("有玩家加入"),
    PLAYER_EXIT("有玩家退出");

    private String value;

    WsMessageTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
