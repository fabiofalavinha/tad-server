package org.religion.umbanda.tad.service.vo;

import org.joda.time.DateTime;
import org.religion.umbanda.tad.model.GenderType;

import java.util.List;

public class CollaboratorVO {

    private String id;
    private String name;
    private String email;
    private List<TelephoneVO> telephones;
    private DateTime startDate;
    private DateTime releaseDate;
    private GenderType genderType;

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

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
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

    public DateTime getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(DateTime releaseDate) {
        this.releaseDate = releaseDate;
    }
}
