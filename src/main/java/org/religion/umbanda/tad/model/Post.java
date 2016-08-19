package org.religion.umbanda.tad.model;

import org.joda.time.DateTime;

import java.util.UUID;

public class Post {

    private UUID id;
    private String title;
    private String content;
    private PostType postType;
    private VisibilityType visibilityType;
    private UserCredentials createdBy;
    private DateTime created;
    private UserCredentials modifiedBy;
    private DateTime modified;
    private UserCredentials publishedBy;
    private DateTime published;
    private int order;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public PostType getPostType() {
        return postType;
    }

    public void setPostType(PostType postType) {
        this.postType = postType;
    }

    public VisibilityType getVisibilityType() {
        return visibilityType;
    }

    public void setVisibilityType(VisibilityType visibilityType) {
        this.visibilityType = visibilityType;
    }

    public UserCredentials getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserCredentials createdBy) {
        this.createdBy = createdBy;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    public UserCredentials getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(UserCredentials modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public DateTime getModified() {
        return modified;
    }

    public void setModified(DateTime modified) {
        this.modified = modified;
    }

    public UserCredentials getPublishedBy() {
        return publishedBy;
    }

    public void setPublishedBy(UserCredentials publishedBy) {
        this.publishedBy = publishedBy;
    }

    public DateTime getPublished() {
        return published;
    }

    public void setPublished(DateTime published) {
        this.published = published;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}