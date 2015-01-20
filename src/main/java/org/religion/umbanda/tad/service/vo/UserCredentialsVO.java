package org.religion.umbanda.tad.service.vo;

import org.religion.umbanda.tad.model.UserRole;

public class UserCredentialsVO {

    private String userName;
    private UserRole userRole;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
