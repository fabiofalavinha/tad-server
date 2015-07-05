package org.religion.umbanda.tad.service.impl;

import org.joda.time.DateTime;
import org.religion.umbanda.tad.model.*;
import org.religion.umbanda.tad.service.CollaboratorRepository;
import org.religion.umbanda.tad.service.CollaboratorService;
import org.religion.umbanda.tad.service.MailService;
import org.religion.umbanda.tad.service.vo.CollaboratorVO;
import org.religion.umbanda.tad.service.vo.TelephoneVO;
import org.religion.umbanda.tad.util.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class CollaboratorServiceImpl implements CollaboratorService {

    @Autowired
    private CollaboratorRepository collaboratorRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private MailTemplateFactory mailTemplateFactory;

    @RequestMapping(value = "/collaborators", method = RequestMethod.GET, produces = "application/json")
    public List<CollaboratorVO> findAll() {
        final List<Collaborator> collaborators = collaboratorRepository.findAll();
        final List<CollaboratorVO> result = new ArrayList<CollaboratorVO>();
        for (Collaborator collaborator : collaborators) {
            final CollaboratorVO vo = new CollaboratorVO();
            vo.setId(collaborator.getPerson().getId().toString());
            vo.setUserRole(collaborator.getUserCredentials().getUserRole());
            vo.setName(collaborator.getPerson().getName());
            vo.setEmail(collaborator.getUserCredentials().getUserName());
            vo.setBirthDate(DateTimeUtils.toString(collaborator.getPerson().getBirthDate()));
            vo.setStartDate(DateTimeUtils.toString(collaborator.getStartDate()));
            vo.setReleaseDate(DateTimeUtils.toString(collaborator.getReleaseDate()));
            vo.setGenderType(collaborator.getPerson().getGenderType());
            vo.setActive(collaborator.getReleaseDate() == null);
            final List<Telephone> telephones = collaborator.getPerson().getTelephones();
            final List<TelephoneVO> telephoneVOs = new ArrayList<TelephoneVO>(telephones.size());
            for (Telephone telephone : telephones) {
                final TelephoneVO telephoneVO = new TelephoneVO();
                telephoneVO.setPhoneType(telephone.getPhoneType());
                telephoneVO.setAreaCode(telephone.getAreaCode());
                telephoneVO.setNumber(telephone.getNumber());
                telephoneVOs.add(telephoneVO);
            }
            vo.setTelephones(telephoneVOs);
            result.add(vo);
        }
        return result;
    }

    @RequestMapping(value = "/collaborator/{id}", method = RequestMethod.DELETE)
    public void removeCollaborator(
            @PathVariable("id") String idString) {
        UUID id;
        try {
            id = UUID.fromString(idString);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("ID do colaborador é inválido", ex);
        }
        collaboratorRepository.removeById(id);
    }

    @RequestMapping(value = "/collaborator", method = RequestMethod.POST)
    public void saveCollaborator(
        @RequestBody CollaboratorVO collaboratorVO) {
        UUID id;
        final String collaboratorId = collaboratorVO.getId();
        if (collaboratorId != null && !"".equals(collaboratorId)) {
            id = UUID.fromString(collaboratorId);
        } else {
            id = UUID.randomUUID();
        }

        final UserCredentials newUserCredentials = new UserCredentials();
        newUserCredentials.setId(id);
        newUserCredentials.setUserRole(collaboratorVO.getUserRole());
        newUserCredentials.setUserName(collaboratorVO.getEmail());
        // newUserCredentials.setPassword(Password.randomPassword());
        newUserCredentials.setPassword(Password.createBySHA1("temp123"));

        final Person newPerson = new Person();
        newPerson.setId(id);
        newPerson.setName(collaboratorVO.getName());
        newPerson.setBirthDate(DateTime.parse(collaboratorVO.getBirthDate()));
        newPerson.setGenderType(collaboratorVO.getGenderType());
        final List<Telephone> newTelephones = new ArrayList<Telephone>();
        for (TelephoneVO telephoneVO : collaboratorVO.getTelephones()) {
            final Telephone newTelephone = new Telephone();
            newTelephone.setId(UUID.randomUUID());
            newTelephone.setPhoneType(telephoneVO.getPhoneType());
            newTelephone.setAreaCode(telephoneVO.getAreaCode());
            newTelephone.setNumber(telephoneVO.getNumber());
            newTelephones.add(newTelephone);
        }
        newPerson.setTelephones(newTelephones);

        final Collaborator newCollaborator = new Collaborator();
        newCollaborator.setPerson(newPerson);
        newCollaborator.setObservation(collaboratorVO.getObservation());

        final String startDateAsString = collaboratorVO.getStartDate();
        if (startDateAsString != null && "".equals(startDateAsString.trim())) {
            newCollaborator.setStartDate(DateTime.parse(startDateAsString));
        }

        final String releaseDateAsString = collaboratorVO.getReleaseDate();
        if (releaseDateAsString != null && !"".equals(releaseDateAsString.trim())) {
            newCollaborator.setReleaseDate(DateTime.parse(releaseDateAsString));
        }

        newCollaborator.setUserCredentials(newUserCredentials);

        if (collaboratorRepository.existsById(id)) {
            collaboratorRepository.updateCollaborator(newCollaborator);
        } else {
            collaboratorRepository.addCollaborator(newCollaborator);
            try {
                final MailMessage mailMessage = mailTemplateFactory.getTemplate("newCollaborator").createMailMessage(newCollaborator);
                mailService.send(mailMessage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
