package org.religion.umbanda.tad.model;

public class NotifyNewsletterUsersPostPublishedContent implements NewsletterContent {

    private final NewsletterUser newsletterUser;
    private final Post post;

    public NotifyNewsletterUsersPostPublishedContent(NewsletterUser newsletterUser, Post post) {
        this.newsletterUser = newsletterUser;
        this.post = post;
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
        return post.getId().toString();
    }
}
