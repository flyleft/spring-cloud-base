package me.jcala.zuul.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhipeng.zuo on 2017/9/12.
 */
@ConfigurationProperties("zuul.ws")
public class ZuulWebSocketProperties {
  private static final Logger logger= LoggerFactory.getLogger (ZuulWebSocketProperties.class);
  private boolean enabled = true;
  private Map<String, WsBrokerage> brokerages = new HashMap<> ();

  public boolean isEnabled() {
    return enabled;
  }

  public Map<String, WsBrokerage> getBrokerages() {
    return brokerages;
  }

  @PostConstruct
  public void init() {
    for (Map.Entry<String,WsBrokerage> entry : this.brokerages.entrySet()) {
      WsBrokerage wsBrokerage = entry.getValue();
      if (!StringUtils.hasText(wsBrokerage.getId())) {
        wsBrokerage.id = entry.getKey();
      }
    }
  }
  public static class WsBrokerage {
    private boolean enabled = true;
    private String id;
    private String[] endPoints;

    public boolean isEnabled() {
      return enabled;
    }

    public void setEnabled(boolean enabled) {
      this.enabled = enabled;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String[] getEndPoints() {
      return endPoints;
    }

    public void setEndPoints(String[] endPoints) {
      this.endPoints = endPoints;
    }

    @Override
    public String toString() {
      return "WsBrokerage{" +
              "enabled=" + enabled +
              ", id='" + id + '\'' +
              ", endPoints=" + Arrays.toString (endPoints) +
              '}';
    }
  }

}
