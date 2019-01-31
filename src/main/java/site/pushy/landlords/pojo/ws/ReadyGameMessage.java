package site.pushy.landlords.pojo.ws;

import lombok.Data;
import site.pushy.landlords.core.enums.WsMessageTypeEnum;

/**
 * 玩家准备消息
 *
 * @author Pushy
 * @since 2019/1/24 12:24
 */
@Data
public class ReadyGameMessage extends Message {

    private String userId;

    public ReadyGameMessage(String userId) {
        this.userId = userId;
    }

    @Override
    public WsMessageTypeEnum getType() {
        return WsMessageTypeEnum.READY_GAME;
    }
}
