package me.jcala.config.client.two;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author flyleft
 * @date 2018/4/24
 */
@SpringBootApplication
@RefreshScope
@RestController
@EnableEurekaClient
public class ConfigClientTwoApplication {

    @Autowired
    private Environment env;

    public static void main(String[] args) {
        new SpringApplicationBuilder(ConfigClientTwoApplication.class).web(true).run(args);
    }

    @GetMapping("/info_by_env")
    public String getInfoByEnv() {
        return env.getProperty("site.info", "undefined");
    }

}
