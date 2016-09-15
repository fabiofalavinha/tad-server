package org.religion.umbanda.tad.service.vo;

import org.religion.umbanda.tad.model.Collaborator;
import org.religion.umbanda.tad.model.Telephone;
import org.religion.umbanda.tad.util.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;

public class CollaboratorParserUtils {

    public static CollaboratorVO parse(Collaborator collaborator) {
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
        vo.setObservation(collaborator.getObservation());
        vo.setContributor(collaborator.getContributor());
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
        return vo;
    }
}
