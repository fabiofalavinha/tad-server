package org.religion.umbanda.tad.model;

public class NotifyNewsletterUsersPostPublishedMailTemplate implements MailTemplate<NewsletterContent> {

    private final String key;
    private final String subject;
    private final String body;

    NotifyNewsletterUsersPostPublishedMailTemplate(String key, String subject, String body) {
        this.key = key;
        this.subject = subject;
        this.body = body;
    }

    public String getKey() {
        return key;
    }

    @Override
    public MailMessage createMailMessage(NewsletterContent newsletterContent) {
        final MailMessage mailMessage = new MailMessage();
        mailMessage.setTo(newsletterContent.getRecipientMail());
        mailMessage.setSubject(subject);
        mailMessage.setType(MailMessageType.HTML);
        mailMessage.setContent(String.format(body, newsletterContent.getRecipientName(), newsletterContent.getContent()));
        return mailMessage;
    }
}
