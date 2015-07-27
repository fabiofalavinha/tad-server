package org.religion.umbanda.tad.service.vo;

public class FinancialReferenceVO {

    private String id;
    private String description;
    private int category;
    private boolean associatedWithCollaborator;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public boolean isAssociatedWithCollaborator() {
        return associatedWithCollaborator;
    }

    public void setAssociatedWithCollaborator(boolean associatedWithCollaborator) {
        this.associatedWithCollaborator = associatedWithCollaborator;
    }
}
