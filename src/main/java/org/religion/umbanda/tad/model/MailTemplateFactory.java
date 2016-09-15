package org.religion.umbanda.tad.model;

import java.util.Hashtable;
import java.util.Map;

public class MailTemplateFactory {

    private final Map<String, MailTemplate<?>> mailTemplateMap;

    public MailTemplateFactory() {
        mailTemplateMap = new Hashtable<>();
    }

    public void addTemplate(MailTemplate mailTemplate) {
        mailTemplateMap.put(mailTemplate.getKey(), mailTemplate);
    }

    public <R> MailTemplate<R> getTemplate(String key) {
        return (MailTemplate<R>) mailTemplateMap.get(key);
    }

}
