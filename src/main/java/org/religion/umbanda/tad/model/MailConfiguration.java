package org.religion.umbanda.tad.model;

import org.religion.umbanda.tad.log.LogFactory;
import org.religion.umbanda.tad.model.financial.FinancialEntryReceiptMailTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@PropertySource("classpath:mail.properties")
public class MailConfiguration {

    @Value("${mail.protocol}")
    private String protocol;

    @Value("${mail.host}")
    private String host;

    @Value("${mail.port}")
    private int port;

    @Value("${mail.smtp.auth}")
    private boolean auth;

    @Value("${mail.smtp.starttls.enable}")
    private boolean starttls;

    @Value("${mail.from.financial}")
    private String fromFinancial;

    @Value("${mail.from.general}")
    private String fromGeneral;

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;

    @Autowired
    private NewCollaboratorMailTemplate newCollaboratorMailTemplate;

    @Autowired
    private ForgotPasswordMailTemplate forgotPasswordMailTemplate;

    @Autowired
    private FinancialEntryReceiptMailTemplate financialEntryReceiptMailTemplate;

    @Autowired
    private NotifyNewsletterUsersPostPublishedMailTemplate notifyNewsletterUsersPostPublishedMailTemplate;

    @Autowired
    private ConfirmNewsletterUserMailTemplate confirmNewsletterUserMailTemplate;

    public String getFromFinancial() {
        return fromFinancial;
    }

    public String getFromGeneral() {
        return fromGeneral;
    }

    @Bean
    public MailTemplateFactory mailTemplateFactory() {

        LogFactory.createLog(this.getClass()).debug("getFromGeneral() ==> " + getFromGeneral());

        final MailTemplateFactory mailTemplateFactory = new MailTemplateFactory(new MailSender(getFromGeneral(), getFromFinancial()));
        mailTemplateFactory.addTemplate(newCollaboratorMailTemplate);
        mailTemplateFactory.addTemplate(forgotPasswordMailTemplate);
        mailTemplateFactory.addTemplate(financialEntryReceiptMailTemplate);
        mailTemplateFactory.addTemplate(notifyNewsletterUsersPostPublishedMailTemplate);
        mailTemplateFactory.addTemplate(confirmNewsletterUserMailTemplate);
        return mailTemplateFactory;
    }

    @Bean
    public JavaMailSender createMailSender() {
        final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        final Properties mailProperties = new Properties();
        mailProperties.put("mail.smtp.auth", auth);
        mailProperties.put("mail.smtp.starttls.enable", starttls);
        mailSender.setJavaMailProperties(mailProperties);
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setProtocol(protocol);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        return mailSender;
    }
}
