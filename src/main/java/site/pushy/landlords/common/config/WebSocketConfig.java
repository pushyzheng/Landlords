package site.pushy.landlords.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import site.pushy.landlords.common.handler.WebSocketPushHandler;

/**
 * @author Fuxing
 * @since 2019/1/2 20:14
 */
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {

    }

    @Bean
    public WebSocketHandler webSocketHandler() {
        return new WebSocketPushHandler();
    }

}
