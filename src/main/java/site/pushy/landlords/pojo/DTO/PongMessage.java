package site.pushy.landlords.pojo.DTO;

import lombok.Data;
import site.pushy.landlords.core.enums.WsMessageTypeEnum;
import site.pushy.landlords.pojo.ws.Message;

import java.util.Date;

/**
 * @author Pushy
 * @since 2019/2/17 19:17
 */
@Data
public class PongMessage extends Message {

    private Date timeStamp;

    public PongMessage() {
        this.timeStamp = new Date();
    }

    @Override
    public WsMessageTypeEnum getType() {
        return WsMessageTypeEnum.PONG;
    }

}
