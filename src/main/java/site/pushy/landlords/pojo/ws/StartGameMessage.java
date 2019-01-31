package site.pushy.landlords.pojo.ws;

import lombok.Data;
import site.pushy.landlords.core.enums.WsMessageTypeEnum;

/**
 * 开始游戏消息
 *
 * @author Pushy
 * @since 2019/1/24 12:08
 */
@Data
public class StartGameMessage extends Message {

    private String roomId;

    public StartGameMessage(String roomId) {
        this.roomId = roomId;
    }

    @Override
    public WsMessageTypeEnum getType() {
        return WsMessageTypeEnum.START_GAME;
    }

}
