package org.religion.umbanda.tad.service;

import org.religion.umbanda.tad.model.UserCredentials;

import java.util.UUID;

public interface UserCredentialsRepository {

    UserCredentials findByUserName(String userName);
    UserCredentials findById(UUID id);
    void update(UserCredentials userCredentials);

}
