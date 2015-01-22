package org.religion.umbanda.tad.service.vo;

import org.joda.time.DateTime;
import org.religion.umbanda.tad.model.GenderType;
import org.religion.umbanda.tad.model.UserRole;

import java.util.List;

public class CollaboratorVO {

    private String id;
    private String name;
    private String email;
    private List<TelephoneVO> telephones;
    private String birthDate;
    private String startDate;
    private String releaseDate;
    private GenderType genderType;
    private UserRole userRole;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public GenderType getGenderType() {
        return genderType;
    }

    public void setGenderType(GenderType genderType) {
        this.genderType = genderType;
    }

    public List<TelephoneVO> getTelephones() {
        return telephones;
    }

    public void setTelephones(List<TelephoneVO> telephones) {
        this.telephones = telephones;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
}
