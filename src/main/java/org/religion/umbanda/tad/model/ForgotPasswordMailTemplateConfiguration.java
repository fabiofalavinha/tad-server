package org.religion.umbanda.tad.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:mail-template.properties")
public class ForgotPasswordMailTemplateConfiguration {

    private static final String KEY = "forgotPassword";

    @Value("${mail.template.forgotPassword.subject}")
    private String subject;

    @Value("${mail.template.forgotPassword.body}")
    private String body;

    @Bean
    public ForgotPasswordMailTemplate forgotPasswordMailTemplate() {
        return new ForgotPasswordMailTemplate(subject, body);
    }
}
