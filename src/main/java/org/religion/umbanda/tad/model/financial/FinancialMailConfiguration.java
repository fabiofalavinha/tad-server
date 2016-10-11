package org.religion.umbanda.tad.model.financial;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@PropertySource("classpath:mail-financial.properties")
public class FinancialMailConfiguration {

    @Value("${financial.mail.protocol}")
    private String protocol;

    @Value("${financial.mail.host}")
    private String host;

    @Value("${financial.mail.port}")
    private int port;

    @Value("${financial.mail.smtp.auth}")
    private boolean auth;

    @Value("${financial.mail.smtp.starttls.enable}")
    private boolean starttls;

    @Value("${financial.mail.from}")
    private String from;

    @Value("${financial.mail.username}")
    private String username;

    @Value("${financial.mail.password}")
    private String password;

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

    public String getFrom() {
        return username;
    }
}
