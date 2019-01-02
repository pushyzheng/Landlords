package site.pushy.landlords.core.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import site.pushy.landlords.common.handler.WebSocketPushHandler;

/**
 * @author Fuxing
 * @since 2019/1/2 20:11
 */
@Component
public class NotifyComponent {

    @Autowired
    private WebSocketPushHandler websocketHandler;

    /*
        1. 发送给某个房间所有的客户端消息
        2. 发送给某个玩家的客户端消息
     */


}
