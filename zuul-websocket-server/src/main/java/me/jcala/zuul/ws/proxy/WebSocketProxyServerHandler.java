package me.jcala.zuul.ws.proxy;

import me.jcala.zuul.ws.ZuulWebSocketProperties;
import me.jcala.zuul.ws.resolver.ZuulPropertiesResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class WebSocketProxyServerHandler extends AbstractWebSocketHandler {
    private static final Logger logger= LoggerFactory.getLogger (WebSocketProxyServerHandler.class);
    private final Map<String, NextHop> nextHops = new ConcurrentHashMap<>();
    private ZuulWebSocketProperties zuulWebSocketProperties;
    private ZuulPropertiesResolver zuulPropertiesResolver;

    public WebSocketProxyServerHandler(ZuulWebSocketProperties zuulWebSocketProperties,
                                       ZuulPropertiesResolver zuulPropertiesResolver) {
        this.zuulWebSocketProperties = zuulWebSocketProperties;
        this.zuulPropertiesResolver = zuulPropertiesResolver;
    }

    @Override
    public void handleMessage(WebSocketSession backSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        throw new RuntimeException ("test");
        //getNextHop(backSession).sendMessageToNextHop(webSocketMessage);
    }

    private NextHop getNextHop(WebSocketSession backSession) {
        NextHop nextHop = nextHops.get(backSession.getId());
        if (nextHop == null) {
            nextHop = new NextHop(zuulWebSocketProperties,zuulPropertiesResolver,backSession);
            nextHops.put(backSession.getId(), nextHop);
        }
        return nextHop;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info ("proxy server session {} ConnectionEstablished",session);
    }


    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        logger.info ("proxy server session {} handleTransportError",session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    }

    @Override
    public boolean supportsPartialMessages() {
        return super.supportsPartialMessages ();
    }
}
