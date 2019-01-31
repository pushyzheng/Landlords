package site.pushy.landlords.pojo.ws;

import site.pushy.landlords.core.enums.WsMessageTypeEnum;

/**
 * @author Pushy
 * @since 2019/1/24 12:32
 */
public abstract class Message {

    public abstract WsMessageTypeEnum getType();

    public String getDescription() {
        WsMessageTypeEnum type = getType();
        return type != null ? type.getValue() : "";
    }

}
