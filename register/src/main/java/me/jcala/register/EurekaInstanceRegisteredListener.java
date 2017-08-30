package me.jcala.register;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.support.MessageBuilder;

/**
 * Created by zhipeng.zuo on 2017/8/28.
 */
@EnableBinding(Source.class)
public class EurekaInstanceRegisteredListener implements ApplicationListener<EurekaInstanceRegisteredEvent> {

  @Autowired
  private Source source;

  @Override
  public void onApplicationEvent(EurekaInstanceRegisteredEvent eurekaInstanceRegisteredEvent) {
    if(eurekaInstanceRegisteredEvent.isReplication()){
      source.output().send(MessageBuilder.withPayload(eurekaInstanceRegisteredEvent.getInstanceInfo()).build());
    }
  }

}
