package org.religion.umbanda.tad.service.vo;

public class PostResponse {

    private String id;
    private String title;
    private String content;
    private String postType;
    private String visibility;
    private UserCredentialsVO createdBy;
    private String created;
    private UserCredentialsVO modifiedBy;
    private String modified;
    private UserCredentialsVO publishedBy;
    private String published;

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

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public UserCredentialsVO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserCredentialsVO createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public UserCredentialsVO getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(UserCredentialsVO modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public UserCredentialsVO getPublishedBy() {
        return publishedBy;
    }

    public void setPublishedBy(UserCredentialsVO publishedBy) {
        this.publishedBy = publishedBy;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }
}
