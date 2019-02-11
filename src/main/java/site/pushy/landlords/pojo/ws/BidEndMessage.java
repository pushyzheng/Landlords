package site.pushy.landlords.pojo.ws;

import lombok.Data;
import site.pushy.landlords.core.enums.WsMessageTypeEnum;

/**
 * @author Pushy
 * @since 2019/2/3 17:56
 */
@Data
public class BidEndMessage extends Message {

    @Override
    public WsMessageTypeEnum getType() {
        return WsMessageTypeEnum.BID_END;
    }
}
