package me.jcala.zuul.ws.socket;

import me.jcala.zuul.ws.filter.ProxyRedirectFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.socket.WebSocketHandler;

/**
 * Created by zhipeng.zuo on 2017/9/12.
 */
@Configuration
@ConditionalOnWebApplication //当前项目是Web项目的条件
@ConditionalOnProperty(prefix = "zuul.ws", name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnClass(WebSocketHandler.class)//当类路径下有指定的类的条件下
@EnableConfigurationProperties(ZuulWebSocketProperties.class)
public class ZuulWebSocketConfiguration implements ApplicationListener<ContextRefreshedEvent> {
  private static final Logger logger= LoggerFactory.getLogger (ZuulWebSocketConfiguration.class);
  private final ZuulWebSocketProperties zuulWebSocketProperties;
  private final ZuulProperties zuulProperties;

  @Autowired
  public ZuulWebSocketConfiguration(ZuulWebSocketProperties zuulWebSocketProperties,
                                    ZuulProperties zuulProperties) {
    this.zuulWebSocketProperties = zuulWebSocketProperties;
    this.zuulProperties = zuulProperties;
  }

  @Bean
  public ProxyRedirectFilter proxyRedirectFilter(RouteLocator routeLocator) {
    return new ProxyRedirectFilter (routeLocator);
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    ignorePattern("**/websocket");
    ignorePattern("**/info");
  }
  private void ignorePattern(String ignoredPattern) {
    for (String pattern : zuulProperties.getIgnoredPatterns()) {
      if (pattern.toLowerCase().contains(ignoredPattern))
        return;
    }
    zuulProperties.getIgnoredPatterns().add(ignoredPattern);
  }

  @Bean
  public RegisterWebSocketHandler registerWebSocketHandler(ZuulWebSocketProperties zuulWebSocketProperties){
    return new RegisterWebSocketHandler (zuulWebSocketProperties);
  }

  @Bean
  @ConditionalOnMissingBean
  public ZuulPropertiesResolver zuulPropertiesResolver(final DiscoveryClient discoveryClient,
                                                       final ZuulProperties zuulProperties){
    return new EurekaPropertiesResolver (discoveryClient,zuulProperties);
  }
}
