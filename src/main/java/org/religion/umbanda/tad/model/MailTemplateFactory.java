package org.religion.umbanda.tad.model;

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
