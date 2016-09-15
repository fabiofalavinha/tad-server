package org.religion.umbanda.tad.service.impl;

import org.religion.umbanda.tad.model.MailConfiguration;
import org.religion.umbanda.tad.model.MailMessage;
import org.religion.umbanda.tad.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    private final MailConfiguration mailConfiguration;

    @Autowired
    public MailServiceImpl(MailConfiguration mailConfiguration) {
        this.mailConfiguration = mailConfiguration;
    }

    @Override
    public void send(MailMessage message) {
        final JavaMailSender javaMailSender = mailConfiguration.createMailSender();
        final SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(message.getTo());
        mailMessage.setFrom(mailConfiguration.getFrom());
        mailMessage.setSubject(message.getSubject());
        mailMessage.setText(message.getText());
        javaMailSender.send(mailMessage);
    }
}
