package org.religion.umbanda.tad.service.vo;

public class ElementDTO {

    private String id;
    private String name;
    private String unit;
    private int quantity;
    private String primaryCollaboratorId;
    private String secondaryCollaboratorId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPrimaryCollaboratorId() {
        return primaryCollaboratorId;
    }

    public void setPrimaryCollaboratorId(String primaryCollaboratorId) {
        this.primaryCollaboratorId = primaryCollaboratorId;
    }

    public String getSecondaryCollaboratorId() {
        return secondaryCollaboratorId;
    }

    public void setSecondaryCollaboratorId(String secondaryCollaboratorId) {
        this.secondaryCollaboratorId = secondaryCollaboratorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
