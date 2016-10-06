package org.religion.umbanda.tad.model;

public class NewCollaboratorMailTemplate implements GeneralMailTemplate<Collaborator> {

    private final String subject;
    private final String body;

    NewCollaboratorMailTemplate(String subject, String body) {
        this.subject = subject;
        this.body = body;
    }

    @Override
    public MailMessage createMailMessage(Collaborator collaborator) {
        final MailMessage mailMessage = new MailMessage();
        mailMessage.setTo(collaborator.getUserCredentials().getUserName());
        mailMessage.setSubject(subject);
        mailMessage.setContent(String.format(body, collaborator.getPerson().getName()));
        return mailMessage;
    }
}
