package site.pushy.landlords.pojo.ws;

import lombok.Data;
import site.pushy.landlords.core.enums.WsMessageTypeEnum;

/**
 * 叫牌消息
 *
 * @author Pushy
 * @since 2019/2/1 20:25
 */
@Data
public class BidMessage extends Message {

    @Override
    public WsMessageTypeEnum getType() {
        return WsMessageTypeEnum.BID;
    }
}
