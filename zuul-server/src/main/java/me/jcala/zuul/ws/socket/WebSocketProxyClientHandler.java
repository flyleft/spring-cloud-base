package me.jcala.zuul.ws.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

/**
 * Copies data from the client to the server session.
 */
public class WebSocketProxyClientHandler extends AbstractWebSocketHandler {
    private static final Logger logger= LoggerFactory.getLogger (WebSocketProxyClientHandler.class);
    private final WebSocketSession serverSession;

    public WebSocketProxyClientHandler(WebSocketSession serverSession) {
        this.serverSession = serverSession;
        logger.info ("new WebSocketProxyClientHandler ...");
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> webSocketMessage) throws Exception {
        serverSession.sendMessage(webSocketMessage);
        logger.info ("serverSession.sendMessage {}",webSocketMessage);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    }
}
