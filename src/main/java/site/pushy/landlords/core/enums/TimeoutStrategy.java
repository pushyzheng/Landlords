package site.pushy.landlords.core.enums;

public enum TimeoutStrategy {

    /**
     * 不处理
     */
    DO_NOT_ANYTHING,

    /**
     * 不出
     */
    PASS,

    /**
     * 出最小的单牌, 如果管的了上家的话
     */
    PLAY_SMALLEST_CARD_IF_EXSITS
}
