package org.religion.umbanda.tad.service;

import org.religion.umbanda.tad.model.UserCredentials;

public interface UserCredentialsRepository {

    UserCredentials findByUserName(String userName);

}
