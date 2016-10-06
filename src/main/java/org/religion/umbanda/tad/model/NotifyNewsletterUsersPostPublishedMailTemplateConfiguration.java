package org.religion.umbanda.tad.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:mail-template.properties")
public class NotifyNewsletterUsersPostPublishedMailTemplateConfiguration {

    @Value("${mail.template.newsletter.post.published.subject}")
    private String subject;

    @Value("${mail.template.newsletter.post.published.body}")
    private String body;

    @Bean
    public NotifyNewsletterUsersPostPublishedMailTemplate notifyNewsletterUsersPostPublishedMailTemplate() {
        return new NotifyNewsletterUsersPostPublishedMailTemplate(subject, body);
    }
}
