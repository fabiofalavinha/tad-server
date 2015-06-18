package org.religion.umbanda.tad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import java.util.Locale;

@ComponentScan
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) {
        final SpringApplication app = new SpringApplication(Application.class);
        Locale.setDefault(new Locale("pt", "BR"));
        app.run(args);
    }

}