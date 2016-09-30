package org.religion.umbanda.tad.service.impl;

import org.religion.umbanda.tad.model.*;
import org.religion.umbanda.tad.service.AuthenticationService;
import org.religion.umbanda.tad.service.CollaboratorRepository;
import org.religion.umbanda.tad.service.MailService;
import org.religion.umbanda.tad.service.UserCredentialsRepository;
import org.religion.umbanda.tad.service.vo.*;
import org.religion.umbanda.tad.validator.MailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    @Autowired
    private CollaboratorRepository collaboratorRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private MailTemplateFactory mailTemplateFactory;

    @Autowired
    private MailValidator mailValidator;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @Override
    public UserCredentialsVO authenticate(
            @RequestBody AuthenticationRequest request) {
        final UserCredentials userCredentials = userCredentialsRepository.findByUserName(request.getUserName());
        if (userCredentials != null) {
            final Password checkPassword = Password.createBySHA1(request.getPassword());
            if (userCredentials.getPassword().equals(checkPassword)) {
                final Collaborator collaborator = collaboratorRepository.findById(userCredentials.getId());
                final UserCredentialsVO result = new UserCredentialsVO();
                result.setId(userCredentials.getId().toString());
                result.setUserName(userCredentials.getUserName());
                result.setUserRole(userCredentials.getUserRole());
                result.setName(collaborator.getPerson().getName());
                return result;
            }
        }
        return null;
    }

    @RequestMapping(value = "/registerPassword", method = RequestMethod.POST, consumes = "application/json")
    @Override
    public void registerPassword(
            @RequestBody NewPasswordRequest request) {
        UUID id;
        try {
            id = UUID.fromString(request.getId());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("ID do usuário é inválido", ex);
        }

        final UserCredentials userCredentials = userCredentialsRepository.findById(id);
        if (userCredentials == null) {
            throw new IllegalStateException("Não foi possível encontrar as credenciais do usuário");
        }

        userCredentials.setPassword(Password.createBySHA1(request.getPassword()));
        userCredentialsRepository.update(userCredentials);
    }

    @RequestMapping(value = "/forgotPassword", method = RequestMethod.POST, consumes = "application/json")
    @Override
    public void forgotPassword(
            @RequestBody ForgotPasswordRequest request) {
        if (!mailValidator.validate(request.getEmail())) {
            throw new IllegalArgumentException("Email inválido");
        }

        final UserCredentials userCredentials = userCredentialsRepository.findByUserName(request.getEmail());
        if (userCredentials == null) {
            throw new IllegalStateException("Não foi possível encontrar as credenciais do usuário");
        }

        final Collaborator collaborator = collaboratorRepository.findById(userCredentials.getId());
        if (collaborator == null) {
            throw new IllegalStateException("Não foi possível encontrar as informações do colaborador");
        }

        final MailMessage mailMessage = mailTemplateFactory.getTemplate("forgotPassword").createMailMessage(collaborator);
        mailService.send(mailMessage);
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST, consumes = "application/json")
    @Override
    public void changePassword(
            @RequestBody ChangePasswordRequest request) {

        UUID id;
        try {
            id = UUID.fromString(request.getId());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("ID do usuário é inválido", ex);
        }

        final UserCredentials userCredentials = userCredentialsRepository.findById(id);
        if (userCredentials == null) {
            throw new IllegalStateException("Não foi possível encontrar as credenciais do usuário");
        }

        if (!userCredentials.getPassword().equals(Password.createBySHA1(request.getOldPassword()))) {
            throw new IllegalStateException("Sua senha atual é inválida");
        }

        userCredentials.setPassword(Password.createBySHA1(request.getNewPassword()));
        userCredentialsRepository.update(userCredentials);
    }

}
