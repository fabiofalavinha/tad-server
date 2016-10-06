package org.religion.umbanda.tad.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:mail-template.properties")
public class NewCollaboratorMailTemplateConfiguration {

    @Value("${mail.template.newCollaborator.subject}")
    private String subject;

    @Value("${mail.template.newCollaborator.body}")
    private String body;

    @Bean
    public NewCollaboratorMailTemplate newCollaboratorMailTemplate() {
        return new NewCollaboratorMailTemplate(subject, body);
    }
}
