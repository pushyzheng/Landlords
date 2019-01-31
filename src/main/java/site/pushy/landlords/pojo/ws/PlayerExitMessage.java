package site.pushy.landlords.pojo.ws;

import lombok.Data;
import site.pushy.landlords.core.enums.WsMessageTypeEnum;
import site.pushy.landlords.pojo.DO.User;

/**
 * 玩家退出消息
 *
 * @author Pushy
 * @since 2019/1/24 13:41
 */
@Data
public class PlayerExitMessage extends Message {

    private User user;

    public PlayerExitMessage(User user) {
        this.user = user;
    }

    @Override
    public WsMessageTypeEnum getType() {
        return WsMessageTypeEnum.PLAYER_EXIT;
    }

}
