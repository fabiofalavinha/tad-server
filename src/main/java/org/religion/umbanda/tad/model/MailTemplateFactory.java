package org.religion.umbanda.tad.model;

import java.util.Hashtable;
import java.util.Map;

public class MailTemplateFactory {

    private final Map<String, MailTemplate<?>> mailTemplateMap;
    private final MailSender mailSender;

    public MailTemplateFactory(MailSender mailSender) {
        mailTemplateMap = new Hashtable<>();
        this.mailSender = mailSender;
    }

    public void addTemplate(MailTemplate mailTemplate) {
        mailTemplateMap.put(mailTemplate.getKey(), mailTemplate);
    }

    public <R> MailTemplate<R> getTemplate(String key) {
        MailTemplate<R> mailTemplate = (MailTemplate<R>) mailTemplateMap.get(key);
        mailTemplate.setSender(mailSender);
        return mailTemplate;
    }

    public MailSender getMailSender() {
        return mailSender;
    }
}
