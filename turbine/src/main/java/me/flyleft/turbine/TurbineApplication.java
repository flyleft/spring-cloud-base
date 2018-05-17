package me.flyleft.turbine;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.stream.EnableTurbineStream;

/**
 * @author flyleft
 * @date 2018/5/15
 */
@SpringBootApplication
@EnableTurbineStream
@EnableHystrixDashboard
public class TurbineApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(TurbineApplication.class).run(args);
    }
}
