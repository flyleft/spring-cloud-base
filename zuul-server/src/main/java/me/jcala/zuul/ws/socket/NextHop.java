package me.jcala.zuul.ws.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * Represents a 'hop' in the proxying chain, establishes a 'client' to
 * communicate with the next server, with a {@link WebSocketProxyClientHandler}
 * to copy data from the 'client' to the supplied 'server' session.
 */
public class NextHop{
    private static final Logger logger= LoggerFactory.getLogger (NextHop.class);
    private final WebSocketSession clientSession;
    private final ZuulWebSocketProperties zuulWebSocketProperties;

    @Autowired
    private ZuulPropertiesResolver zuulPropertiesResolver;

    public NextHop(WebSocketSession serverSession,
                   ZuulWebSocketProperties zuulWebSocketProperties) {
        this.clientSession = createWebSocketClientSession(serverSession);
        this.zuulWebSocketProperties = zuulWebSocketProperties;
    }

    private WebSocketSession createWebSocketClientSession(WebSocketSession serverSession) {
        URI sessionUri = serverSession.getUri();
        ZuulWebSocketProperties.WsBrokerage wsBrokerage = getWebSocketBrokarage(
                sessionUri);

        Assert.notNull(wsBrokerage, "wsBrokerage");

        String path = getWebSocketServerPath(wsBrokerage, sessionUri);
        Assert.notNull(path, "Web socket uri path");

        String routeHost = zuulPropertiesResolver.getRouteHost(wsBrokerage);
        Assert.notNull(routeHost, "routeHost");

        String uri = ServletUriComponentsBuilder.fromHttpUrl(routeHost).path(path)
                .toUriString();

        try {
            return new StandardWebSocketClient ()
                    .doHandshake(new WebSocketProxyClientHandler (serverSession),
                            uri)
                    .get(1000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessageToNextHop(WebSocketMessage<?> webSocketMessage) throws IOException {
        logger.info ("clientSession.sendMessage {}",webSocketMessage);
        clientSession.sendMessage(webSocketMessage);
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
