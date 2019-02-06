package site.pushy.landlords.pojo.ws;

import site.pushy.landlords.core.enums.IdentityEnum;
import site.pushy.landlords.core.enums.WsMessageTypeEnum;

/**
 * @author Pushy
 * @since 2019/2/5 22:39
 */
public class GameEndMessage extends Message {

    private IdentityEnum winIdentity;  // 胜利方的身份

    public GameEndMessage(IdentityEnum winIdentity) {
        this.winIdentity = winIdentity;
    }

    @Override
    public WsMessageTypeEnum getType() {
        return WsMessageTypeEnum.GAME_END;
    }
}
