package org.religion.umbanda.tad.model;

public class ConfirmNewsletterUserContent implements NewsletterContent {

    private final NewsletterUser newsletterUser;

    public ConfirmNewsletterUserContent(NewsletterUser newsletterUser) {
        this.newsletterUser = newsletterUser;
    }

    @Override
    public String getRecipientMail() {
        return newsletterUser.getEmail();
    }

    @Override
    public String getRecipientName() {
        return newsletterUser.getName();
    }

    @Override
    public String getContent() {
        return newsletterUser.getId().toString();
    }
}
