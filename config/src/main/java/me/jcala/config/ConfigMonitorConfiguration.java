package me.jcala.config;

import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

/**
 * Created by xausky on 3/29/17.
 */
@Profile("config-monitor")
@Configuration
@Import(RabbitAutoConfiguration.class)
public class ConfigMonitorConfiguration {
}
