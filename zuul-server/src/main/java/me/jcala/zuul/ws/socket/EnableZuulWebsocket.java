package me.jcala.zuul.ws.socket;

import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created by zhipeng.zuo on 2017/9/12.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableZuulProxy
@Import(ZuulWebSocketConfiguration.class)
public @interface EnableZuulWebsocket {
}
