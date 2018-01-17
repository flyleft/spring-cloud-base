package me.jcala.zuul.ws.proxy;

import me.jcala.zuul.ws.ZuulWebSocketProperties;
import me.jcala.zuul.ws.resolver.ZuulPropertiesResolver;
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
  private final ZuulPropertiesResolver zuulPropertiesResolver;


  public RegisterWebSocketHandler(ZuulWebSocketProperties zuulWebSocketProperties,
                                  ZuulPropertiesResolver zuulPropertiesResolver) {
    this.zuulWebSocketProperties = zuulWebSocketProperties;
    this.zuulPropertiesResolver = zuulPropertiesResolver;
  }

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    for (Map.Entry<String, ZuulWebSocketProperties.WsBrokerage> entry :
            zuulWebSocketProperties.getBrokerages().entrySet()) {
      ZuulWebSocketProperties.WsBrokerage wsBrokerage = entry.getValue();
      if (wsBrokerage.isEnabled()) {
        registry.addHandler (new WebSocketProxyServerHandler (zuulWebSocketProperties,
                zuulPropertiesResolver),wsBrokerage.getEndPoints ())
                .setAllowedOrigins ("*");
      }
    }
  }
}
