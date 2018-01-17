package me.jcala.zuul.ws.resolver;

import me.jcala.zuul.ws.ZuulWebSocketProperties;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by zhipeng.zuo on 2017/9/12.
 */
public class EurekaPropertiesResolver implements ZuulPropertiesResolver {
  private DiscoveryClient discoveryClient;
  private ZuulProperties zuulProperties;

  public EurekaPropertiesResolver(DiscoveryClient discoveryClient, ZuulProperties zuulProperties) {
    this.discoveryClient = discoveryClient;
    this.zuulProperties = zuulProperties;
  }


  @Override
  public String getRouteHost(ZuulWebSocketProperties.WsBrokerage wsBrokerage) {
    ZuulProperties.ZuulRoute zuulRoute = zuulProperties.getRoutes().get(wsBrokerage.getId());
    if (zuulRoute == null || StringUtils.isEmpty(zuulRoute.getServiceId())) return null;

    List<ServiceInstance> instances = discoveryClient.getInstances(zuulRoute.getServiceId());
    ServiceInstance serviceInstance = instances.get(0);
    return serviceInstance.getUri().toString();
  }
}