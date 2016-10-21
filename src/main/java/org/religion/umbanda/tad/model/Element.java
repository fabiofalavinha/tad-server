package org.religion.umbanda.tad.model;

import java.util.UUID;

public class Element {

    private final String name;

    private UUID id;
    private String unit;
    private int quantity;
    private Collaborator primary;
    private Collaborator secondary;

    public Element(String name) {
        this.name = name;
        id = UUID.randomUUID();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Collaborator getPrimary() {
        return primary;
    }

    public void setPrimary(Collaborator primary) {
        this.primary = primary;
    }

    public Collaborator getSecondary() {
        return secondary;
    }

    public void setSecondary(Collaborator secondary) {
        this.secondary = secondary;
    }
}
