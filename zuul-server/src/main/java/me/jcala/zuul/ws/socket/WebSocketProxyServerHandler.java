package me.jcala.zuul.ws.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles establishment and tracking of next 'hop', and
 * copies data from the current session to the next hop.
 */
@Component
public class WebSocketProxyServerHandler extends AbstractWebSocketHandler {
    private static final Logger logger= LoggerFactory.getLogger (WebSocketProxyServerHandler.class);
    private final Map<String, NextHop> nextHops = new ConcurrentHashMap<>();

    @Autowired
    private ZuulWebSocketProperties webSocketProperties;

    public WebSocketProxyServerHandler() {
        logger.info ("=============init WebSocketProxyServerHandler");
    }

    @Override
    public void handleMessage(WebSocketSession serverSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        getNextHop(serverSession).sendMessageToNextHop(webSocketMessage);
    }

    private NextHop getNextHop(WebSocketSession serverSession) {
        NextHop nextHop = nextHops.get(serverSession.getId());
        if (nextHop == null) {
            nextHop = new NextHop(serverSession,webSocketProperties);
            nextHops.put(serverSession.getId(), nextHop);
        }
        return nextHop;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info ("============afterConnectionEstablished");
    }


    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        logger.info ("============handleTransportError");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.info ("============afterConnectionClosed");
    }

    @Override
    public boolean supportsPartialMessages() {
        return super.supportsPartialMessages ();
    }
}
