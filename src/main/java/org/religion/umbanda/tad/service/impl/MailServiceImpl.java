package org.religion.umbanda.tad.service.impl;

import org.religion.umbanda.tad.model.GeneralMailConfiguration;
import org.religion.umbanda.tad.model.MailMessage;
import org.religion.umbanda.tad.model.MailMessageSenderType;
import org.religion.umbanda.tad.model.MailMessageType;
import org.religion.umbanda.tad.model.financial.FinancialMailConfiguration;
import org.religion.umbanda.tad.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
public class MailServiceImpl implements MailService {

    private final GeneralMailConfiguration generalMailConfiguration;
    private final FinancialMailConfiguration financialMailConfiguration;

    @Autowired
    public MailServiceImpl(GeneralMailConfiguration generalMailConfiguration, FinancialMailConfiguration financialMailConfiguration) {
        this.generalMailConfiguration = generalMailConfiguration;
        this.financialMailConfiguration = financialMailConfiguration;
    }

    private MailSender getMailSender(MailMessage mailMessage) {
        final MailMessageSenderType senderType = mailMessage.getSenderType();
        if (senderType == MailMessageSenderType.GENERAL) {
            return new MailSender(generalMailConfiguration.getFrom(), generalMailConfiguration.createMailSender());
        } else if (senderType == MailMessageSenderType.FINANCIAL) {
            return new MailSender(financialMailConfiguration.getFrom(), financialMailConfiguration.createMailSender());
        } else {
            throw new IllegalStateException(String.format("Could not find mail sender by type [%s]", senderType));
        }
    }

    @Override
    public void send(MailMessage message) {
        final MailSender mailSender = getMailSender(message);
        final JavaMailSender javaMailSender = mailSender.getJavaMailSender();
        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            mimeMessage.addFrom(new InternetAddress[] { new InternetAddress(mailSender.getFrom()) });
            mimeMessage.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(message.getTo()));
            mimeMessage.setSubject(message.getSubject(), "UTF-8");
            if (message.getType() == MailMessageType.HTML) {
                mimeMessage.setContent(message.getContent(), "text/html; charset=utf-8");
            } else {
                mimeMessage.setText(message.getContent(), "UTF-8");
            }
        } catch (MessagingException e) {
            throw new IllegalStateException("Error creating mime message", e);
        }
        javaMailSender.send(mimeMessage);
    }

    private class MailSender {

        private final String from;
        private final JavaMailSender javaMailSender;

        private MailSender(String from, JavaMailSender javaMailSender) {
            this.from = from;
            this.javaMailSender = javaMailSender;
        }

        public String getFrom() {
            return from;
        }

        public JavaMailSender getJavaMailSender() {
            return javaMailSender;
        }
    }
}
