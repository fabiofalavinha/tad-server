package org.religion.umbanda.tad.model;

public class ForgotPasswordMailTemplate implements MailTemplate<Collaborator> {

    private final String key;
    private final String subject;
    private final String body;

    public ForgotPasswordMailTemplate(String key, String subject, String body) {
        this.key = key;
        this.subject = subject;
        this.body = body;
    }

    public String getKey() {
        return key;
    }

    @Override
    public MailMessage createMailMessage(Collaborator collaborator) {
        final MailMessage mailMessage = new MailMessage();
        mailMessage.setTo(collaborator.getUserCredentials().getUserName());
        mailMessage.setSubject(subject);
        mailMessage.setText(String.format(body, collaborator.getPerson().getName(), collaborator.getPerson().getId()));
        return mailMessage;
    }

}