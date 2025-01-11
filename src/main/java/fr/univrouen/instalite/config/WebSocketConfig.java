package fr.univrouen.instalite.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Configurer un broker de message
        config.enableSimpleBroker("/topic"); // Destination des messages
        config.setApplicationDestinationPrefixes("/app"); // Préfixe pour les messages envoyés
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Configurer le point de terminaison WebSocket
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }
}
