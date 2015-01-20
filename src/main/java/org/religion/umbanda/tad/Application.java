package org.religion.umbanda.tad;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
        final SpringApplication app = new SpringApplication(Application.class);
        app.run(args);
    }

}