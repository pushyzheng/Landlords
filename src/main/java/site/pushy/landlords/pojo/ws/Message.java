package site.pushy.landlords.pojo.ws;

import site.pushy.landlords.core.enums.WsMessageTypeEnum;

/**
 * WebSocket 消息的抽象
 *
 * @author Pushy
 * @since 2019/1/24 12:32
 */
public abstract class Message {

    public String getDescription() {
        WsMessageTypeEnum type = getType();
        return type != null ? type.getValue() : "";
    }

    public abstract WsMessageTypeEnum getType();
}
