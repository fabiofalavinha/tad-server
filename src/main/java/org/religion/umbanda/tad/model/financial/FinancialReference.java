package org.religion.umbanda.tad.model.financial;

public class FinancialReference {

    private String id;
    private String description;
    private Category category;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isAssociatedWithCollaborator() {
        return associatedWithCollaborator;
    }

    public void setAssociatedWithCollaborator(boolean associatedWithCollaborator) {
        this.associatedWithCollaborator = associatedWithCollaborator;
    }

}
