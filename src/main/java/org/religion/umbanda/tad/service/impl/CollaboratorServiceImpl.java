package org.religion.umbanda.tad.service.impl;

import org.religion.umbanda.tad.model.Collaborator;
import org.religion.umbanda.tad.model.Telephone;
import org.religion.umbanda.tad.service.CollaboratorRepository;
import org.religion.umbanda.tad.service.CollaboratorService;
import org.religion.umbanda.tad.service.vo.CollaboratorVO;
import org.religion.umbanda.tad.service.vo.TelephoneVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CollaboratorServiceImpl implements CollaboratorService {

    @Autowired
    private CollaboratorRepository collaboratorRepository;

    @RequestMapping(value = "/collaborators", method = RequestMethod.GET, produces = "application/json")
    public List<CollaboratorVO> findAll() {
        final List<Collaborator> collaborators = collaboratorRepository.findAll();
        final List<CollaboratorVO> result = new ArrayList<CollaboratorVO>();
        for (Collaborator collaborator : collaborators) {
            final CollaboratorVO vo = new CollaboratorVO();
            vo.setId(collaborator.getPerson().getId().toString());
            vo.setName(collaborator.getPerson().getName());
            vo.setEmail(collaborator.getUserCredentials().getUserName());
            vo.setActive(collaborator.getReleaseDate() == null);
            vo.setStartDate(collaborator.getStartDate());
            vo.setGenderType(collaborator.getPerson().getGenderType());
            final List<Telephone> telephones = collaborator.getPerson().getTelephones();
            final List<TelephoneVO> telephoneVOs = new ArrayList<TelephoneVO>(telephones.size());
            for (Telephone telephone : telephones) {
                final TelephoneVO telephoneVO = new TelephoneVO();
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
            @PathVariable("id") String id) {
        collaboratorRepository.removeById(id);
    }

}
