package org.religion.umbanda.tad.service.impl;

import org.religion.umbanda.tad.model.Password;
import org.religion.umbanda.tad.model.UserCredentials;
import org.religion.umbanda.tad.service.AuthenticationService;
import org.religion.umbanda.tad.service.UserCredentialsRepository;
import org.religion.umbanda.tad.service.vo.AuthenticationRequest;
import org.religion.umbanda.tad.service.vo.UserCredentialsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("/security")
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public UserCredentialsVO authenticate(
            @RequestBody AuthenticationRequest request) {
        final UserCredentials userCredentials = userCredentialsRepository.findByUserName(request.getUserName());
        if (userCredentials != null) {
            final Password checkPassword = Password.createBySHA1(request.getPassword());
            if (userCredentials.getPassword().equals(checkPassword)) {
                final UserCredentialsVO result = new UserCredentialsVO();
                result.setUserName(userCredentials.getUserName());
                result.setUserRole(userCredentials.getUserRole());
                return result;
            }
        }
        return null;
    }

}
