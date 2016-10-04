package org.religion.umbanda.tad.model;

public class NewCollaboratorMailTemplate implements MailTemplate<Collaborator> {

    private final String key;
    private final String subject;
    private final String body;

    private MailSender mailSender;

    public NewCollaboratorMailTemplate(String key, String subject, String body) {
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
        mailMessage.setFrom(mailSender.getGeneral());
        mailMessage.setTo(collaborator.getUserCredentials().getUserName());
        mailMessage.setSubject(subject);
        mailMessage.setContent(String.format(body, collaborator.getPerson().getName()));
        return mailMessage;
    }

    @Override
    public void setSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }
}
