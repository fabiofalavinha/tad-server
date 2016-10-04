package org.religion.umbanda.tad.model;

public class MailMessage {

    private String from;
    private String to;
    private String subject;
    private String content;
    private MailMessageType type;

    public MailMessage() {
        type = MailMessageType.TEXT;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MailMessageType getType() {
        return type;
    }

    public void setType(MailMessageType type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
