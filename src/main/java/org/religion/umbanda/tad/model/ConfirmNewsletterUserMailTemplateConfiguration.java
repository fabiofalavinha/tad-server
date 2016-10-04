package org.religion.umbanda.tad.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:mail-template.properties")
public class ConfirmNewsletterUserMailTemplateConfiguration {

    public static final String KEY = "confirmNewsletterUserMail";

    @Value("${mail.template.newsletter.user.confirmation.subject}")
    private String subject;

    @Value("${mail.template.newsletter.user.confirmation.body}")
    private String body;

    @Bean
    public ConfirmNewsletterUserMailTemplate confirmNewsletterUserMailTemplate() {
        return new ConfirmNewsletterUserMailTemplate(KEY, subject, body);
    }

}
