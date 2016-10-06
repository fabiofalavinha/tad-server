package org.religion.umbanda.tad.model;

public class ConfirmNewsletterUserMailTemplate implements GeneralMailTemplate<NewsletterContent> {

    private final String subject;
    private final String body;

    ConfirmNewsletterUserMailTemplate(String subject, String body) {
        this.subject = subject;
        this.body = body;
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
