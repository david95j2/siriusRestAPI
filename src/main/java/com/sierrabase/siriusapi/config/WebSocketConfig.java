package com.sierrabase.siriusapi.config;

import com.sierrabase.siriusapi.websocket.AbstractWebSocketHandler;
import com.sierrabase.siriusapi.websocket.UserIdHandshakeInterceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final AbstractWebSocketHandler fitAreaHandler;
    private final AbstractWebSocketHandler segmentationHandler;

    public WebSocketConfig(
            @Qualifier("fitAreaWebSocketHandler") AbstractWebSocketHandler fitAreaHandler,
            @Qualifier("segmentationWebSocketHandler") AbstractWebSocketHandler segmentationHandler
    ) {
        this.fitAreaHandler = fitAreaHandler;
        this.segmentationHandler = segmentationHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(fitAreaHandler, "/ws/maps/{loginId}")
                .addInterceptors(new UserIdHandshakeInterceptor()) // 인터셉터 추가
                .setAllowedOrigins("*");
        registry.addHandler(segmentationHandler, "/ws/albums/{loginId}/analyses")
                .addInterceptors(new UserIdHandshakeInterceptor()) // 인터셉터 추가
                .setAllowedOrigins("*");
    }
}
