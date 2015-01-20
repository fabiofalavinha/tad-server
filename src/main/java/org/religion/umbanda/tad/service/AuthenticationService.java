package org.religion.umbanda.tad.service;

import org.religion.umbanda.tad.service.vo.AuthenticationRequest;
import org.religion.umbanda.tad.service.vo.UserCredentialsVO;

public interface AuthenticationService {

    UserCredentialsVO authenticate(AuthenticationRequest request);

}
