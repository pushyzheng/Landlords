package site.pushy.landlords.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import site.pushy.landlords.common.handler.WebSocketPushHandler;
import site.pushy.landlords.common.interceptor.WebSocketInterceptor;

/**
 * @author Fuxing
 * @since 2019/1/2 20:14
 */
@Configuration
@EnableWebSocket  // 开启websocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 将WebSocketPushHandler映射到 /webSocketServer
        registry.addHandler(WebSocketPushHandler(), "/webSocketServer")
                .addInterceptors(new WebSocketInterceptor());
    }

    @Bean
    public WebSocketHandler WebSocketPushHandler() {
        return new WebSocketPushHandler();
    }

}
