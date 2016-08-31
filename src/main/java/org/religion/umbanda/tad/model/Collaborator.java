package org.religion.umbanda.tad.model;

import org.joda.time.DateTime;

public class Collaborator {

    private Person person;
    private DateTime startDate;
    private DateTime releaseDate;
    private String observation;
    private UserCredentials userCredentials;
    private boolean contributor;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public DateTime getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(DateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public UserCredentials getUserCredentials() {
        return userCredentials;
    }

    public void setUserCredentials(UserCredentials userCredentials) {
        this.userCredentials = userCredentials;
    }

    public boolean getContributor() { return contributor; }

    public void setContributor(boolean contributor) { this.contributor = contributor; }
}
