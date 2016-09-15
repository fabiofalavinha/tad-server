package org.religion.umbanda.tad.service.impl;

import org.religion.umbanda.tad.model.MailConfiguration;
import org.religion.umbanda.tad.model.MailMessage;
import org.religion.umbanda.tad.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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
        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            mimeMessage.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(message.getTo()));
            mimeMessage.addFrom(new InternetAddress[] { new InternetAddress(mailConfiguration.getFrom()) });
            mimeMessage.setSubject(message.getSubject(), "UTF-8");
            mimeMessage.setText(message.getText(), "UTF-8");

            final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
            helper.setTo(message.getTo());
            helper.setFrom(mailConfiguration.getFrom());
        } catch (MessagingException e) {
            throw new IllegalStateException("Error creating mime message", e);
        }
        /*
        final SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(message.getTo());
        mailMessage.setFrom(mailConfiguration.getFrom());
        mailMessage.setSubject(message.getSubject());
        mailMessage.setText(message.getText());
        */
        javaMailSender.send(mimeMessage);
    }
}
