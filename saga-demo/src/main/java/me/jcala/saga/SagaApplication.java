package me.jcala.saga;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SagaApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SagaApplication.class).web(true).run(args);
    }

}
