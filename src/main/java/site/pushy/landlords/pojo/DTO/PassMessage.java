package site.pushy.landlords.pojo.DTO;

import lombok.Data;
import site.pushy.landlords.core.enums.WsMessageTypeEnum;
import site.pushy.landlords.pojo.DO.User;
import site.pushy.landlords.pojo.ws.Message;

/**
 * @author Pushy
 * @since 2019/2/18 22:03
 */
@Data
public class PassMessage extends Message {

    private User user;

    public PassMessage(User user) {
        this.user = user;
    }

    @Override
    public WsMessageTypeEnum getType() {
        return WsMessageTypeEnum.PASS;
    }
}
