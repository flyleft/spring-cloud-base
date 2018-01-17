package me.jcala.zuul.ws.proxy;

import me.jcala.zuul.ws.ZuulWebSocketProperties;
import me.jcala.zuul.ws.resolver.ZuulPropertiesResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class NextHop{
    private static final Logger logger= LoggerFactory.getLogger (NextHop.class);
    private  WebSocketSession sendSession;

    private ZuulWebSocketProperties zuulWebSocketProperties;

    private ZuulPropertiesResolver zuulPropertiesResolver;

    public NextHop(ZuulWebSocketProperties zuulWebSocketProperties,
                   ZuulPropertiesResolver zuulPropertiesResolver,
                   WebSocketSession backSession) {
        this.zuulWebSocketProperties = zuulWebSocketProperties;
        this.zuulPropertiesResolver = zuulPropertiesResolver;
        this.sendSession = createWebSocketClientSession(backSession);
    }

    private WebSocketSession createWebSocketClientSession(WebSocketSession backSession) {
        URI sessionUri = backSession.getUri();
        ZuulWebSocketProperties.WsBrokerage wsBrokerage = getWebSocketBrokarage(
                sessionUri);
        Assert.notNull(wsBrokerage, "wsBrokerage");

        String path = getWebSocketServerPath(wsBrokerage, sessionUri);
        Assert.notNull(path, "Web socket uri path");

        String routeHost = zuulPropertiesResolver.getRouteHost(wsBrokerage);
        Assert.notNull(routeHost, "routeHost");

        String uri = ServletUriComponentsBuilder.fromHttpUrl(routeHost).path(path)
                .toUriString().replaceFirst ("http","ws");
        try {
            return new StandardWebSocketClient ()
                    .doHandshake(new WebSocketProxyClientHandler (backSession),
                            uri)
                    .get(1000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessageToNextHop(WebSocketMessage<?> webSocketMessage) throws IOException {
        logger.info ("sendSession: {} local: {} remote: {} msg: {}",sendSession,
                sendSession.getLocalAddress (),sendSession.getRemoteAddress (),
                webSocketMessage.getPayload ().toString ());
        sendSession.sendMessage(webSocketMessage);
    }

    /**
     * 根据uri获取对应的WsBrokerage
     */
    private ZuulWebSocketProperties.WsBrokerage getWebSocketBrokarage(URI uri) {
        String path = uri.toString();
        if (path.contains(":")) {
            path = UriComponentsBuilder.fromUriString(path).build().getPath();
        }
        for (Map.Entry<String, ZuulWebSocketProperties.WsBrokerage> entry : zuulWebSocketProperties
                .getBrokerages().entrySet()) {
            ZuulWebSocketProperties.WsBrokerage wsBrokerage = entry.getValue();
            if (wsBrokerage.isEnabled()) {
                for (String endPoint : wsBrokerage.getEndPoints()) {
                    if (PatternMatchUtils.simpleMatch(toPattern(endPoint), path + "/")) {
                        return wsBrokerage;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 根据uri获取对应的endpoint
     */
    private String getWebSocketServerPath(ZuulWebSocketProperties.WsBrokerage wsBrokerage,
                                          URI uri) {
        String path = uri.toString();
        if (path.contains(":")) {
            path = UriComponentsBuilder.fromUriString(path).build().getPath();
        }
        for (String endPoint : wsBrokerage.getEndPoints()) {
            if (PatternMatchUtils.simpleMatch(toPattern(endPoint), path + "/")) {
                return endPoint;
            }
        }
        return null;
    }

    private String toPattern(String path) {
        path = path.startsWith("/") ? "**" + path : "**/" + path;
        return path.endsWith("/") ? path + "**" : path + "/**";
    }
}
