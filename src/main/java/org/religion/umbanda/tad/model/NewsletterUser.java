package org.religion.umbanda.tad.model;

import java.util.UUID;

public class NewsletterUser {

    private UUID id;
    private String name;
    private String email;

    public NewsletterUser() {
        id = UUID.randomUUID();
    }

    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }
}