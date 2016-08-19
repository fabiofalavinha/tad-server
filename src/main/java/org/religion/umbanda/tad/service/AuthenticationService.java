package org.religion.umbanda.tad.service;

import org.religion.umbanda.tad.service.vo.*;

public interface AuthenticationService {

    UserCredentialsVO authenticate(AuthenticationRequest request);

    void registerPassword(NewPasswordRequest request);

    void forgotPassword(ForgotPasswordRequest request);

    void changePassword(ChangePasswordRequest request);

}
