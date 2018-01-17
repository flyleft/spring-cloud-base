package me.jcala.zuul.ws;

import org.springframework.context.annotation.Import;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import java.lang.annotation.*;

/**
 * Created by zhipeng.zuo on 2017/9/12.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableWebSocket
@Import(ZuulWebSocketConfiguration.class)
public @interface EnableZuulWebsocket {
}
