package org.religion.umbanda.tad.model;

import java.util.UUID;
import org.religion.umbanda.tad.model.Password;

public class UserCredentials {
    
    private UUID id;
    private String userName;
    private Password password;
    private UserRole userRole;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}