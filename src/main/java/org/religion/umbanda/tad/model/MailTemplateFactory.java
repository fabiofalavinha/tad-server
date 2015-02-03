package org.religion.umbanda.tad.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.Hashtable;
import java.util.Map;

public class MailTemplateFactory {

    private final Map<String, MailTemplate> mailTemplateMap;

    public MailTemplateFactory() {
        mailTemplateMap = new Hashtable<String, MailTemplate>();
    }

    public void addTemplate(MailTemplate mailTemplate) {
        mailTemplateMap.put(mailTemplate.getKey(), mailTemplate);
    }

    public MailTemplate getTemplate(String key) {
        return mailTemplateMap.get(key);
    }

}
