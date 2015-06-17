package org.religion.umbanda.tad.service.vo;

import org.religion.umbanda.tad.model.VisibilityType;

public class PostRequest {

    private String id;
    private String title;
    private String content;
    private String created;
    private UserCredentialsVO createdBy;
    private String modified;
    private UserCredentialsVO modifiedBy;
    private String published;
    private UserCredentialsVO publishedBy;
    private VisibilityType visibility;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public UserCredentialsVO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserCredentialsVO createdBy) {
        this.createdBy = createdBy;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public UserCredentialsVO getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(UserCredentialsVO modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public UserCredentialsVO getPublishedBy() {
        return publishedBy;
    }

    public void setPublishedBy(UserCredentialsVO publishedBy) {
        this.publishedBy = publishedBy;
    }

    public VisibilityType getVisibility() {
        return visibility;
    }

    public void setVisibility(VisibilityType visibility) {
        this.visibility = visibility;
    }
}
