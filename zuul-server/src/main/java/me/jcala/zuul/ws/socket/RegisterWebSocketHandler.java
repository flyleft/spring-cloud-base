package me.jcala.zuul.ws.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.util.Map;

/**
 * Created by zhipeng.zuo on 2017/9/13.
 */
public class RegisterWebSocketHandler implements WebSocketConfigurer {
  private static final Logger logger= LoggerFactory.getLogger (RegisterWebSocketHandler.class);

  private final ZuulWebSocketProperties zuulWebSocketProperties;


  public RegisterWebSocketHandler(ZuulWebSocketProperties zuulWebSocketProperties) {
    this.zuulWebSocketProperties = zuulWebSocketProperties;
  }

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    for (Map.Entry<String, ZuulWebSocketProperties.WsBrokerage> entry :
            zuulWebSocketProperties.getBrokerages().entrySet()) {
      ZuulWebSocketProperties.WsBrokerage wsBrokerage = entry.getValue();
      logger.info ("======registerWebSocketHandlers: v {}",entry.getValue ());
      if (wsBrokerage.isEnabled()) {
        logger.info ("======registerWebSocketHandlers: new WebSocketProxyServerHandler");
        registry.addHandler (new WebSocketProxyServerHandler (),wsBrokerage.getEndPoints ())
                .setAllowedOrigins ("*");
      }
    }
  }
}
