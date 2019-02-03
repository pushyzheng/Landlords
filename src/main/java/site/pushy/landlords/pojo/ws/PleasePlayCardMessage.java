package site.pushy.landlords.pojo.ws;

import site.pushy.landlords.core.enums.WsMessageTypeEnum;

/**
 * 出牌消息
 *
 * @author Pushy
 * @since 2019/2/3 19:22
 */
public class PleasePlayCardMessage extends Message {

    @Override
    public WsMessageTypeEnum getType() {
        return WsMessageTypeEnum.PLEASE_PLAY_CARD;
    }

}
