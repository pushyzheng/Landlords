package site.pushy.landlords.pojo.ws;

import lombok.Data;
import site.pushy.landlords.core.enums.WsMessageTypeEnum;

/**
 * @author Pushy
 * @since 2019/2/10 23:51
 */
@Data
public class UnReadyGameMessage extends Message {

    private String userId;

    public UnReadyGameMessage(String userId) {
        this.userId = userId;
    }

    @Override
    public WsMessageTypeEnum getType() {
        return WsMessageTypeEnum.UNREADY_GAME;
    }
}
