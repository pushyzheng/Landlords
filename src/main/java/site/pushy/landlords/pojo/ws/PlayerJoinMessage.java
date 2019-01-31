package site.pushy.landlords.pojo.ws;

import lombok.Data;
import site.pushy.landlords.core.enums.WsMessageTypeEnum;
import site.pushy.landlords.pojo.DO.User;

/**
 * 玩家加入消息
 *
 * @author Pushy
 * @since 2019/1/24 13:40
 */
@Data
public class PlayerJoinMessage extends Message {

    private User user;

    public PlayerJoinMessage(User user) {
        this.user = user;
    }

    @Override
    public WsMessageTypeEnum getType() {
        return WsMessageTypeEnum.PLAYER_JOIN;
    }

}
