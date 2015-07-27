package org.religion.umbanda.tad.service.impl;

import org.religion.umbanda.tad.model.financial.Category;
import org.religion.umbanda.tad.model.financial.FinancialReference;
import org.religion.umbanda.tad.service.FinancialReferenceRepository;
import org.religion.umbanda.tad.service.FinancialService;
import org.religion.umbanda.tad.service.vo.FinancialReferenceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class FinancialServiceImpl implements FinancialService {

    @Autowired
    private FinancialReferenceRepository financialReferenceRepository;

    private FinancialReferenceVO convertFinancialReference(FinancialReference financialReference) {
        final FinancialReferenceVO vo = new FinancialReferenceVO();
        vo.setId(financialReference.getId());
        vo.setDescription(financialReference.getDescription());
        vo.setCategory(financialReference.getCategory().getValue());
        vo.setAssociatedWithCollaborator(financialReference.isAssociatedWithCollaborator());
        return vo;
    }

    @RequestMapping(value = "/financial/references", method = RequestMethod.GET, produces = "application/json")
    @Override
    public List<FinancialReferenceVO> getFinancialReferences() {
        final List<FinancialReferenceVO> list = new ArrayList<>();
        for (FinancialReference financialReference : financialReferenceRepository.findAll()) {
            list.add(convertFinancialReference(financialReference));
        }
        return list;
    }

    @RequestMapping(value = "/financial/reference", method = RequestMethod.POST)
    @Override
    public void saveFinancialReference(
        @RequestBody FinancialReferenceVO financialReferenceVO) {
        final String id = financialReferenceVO.getId();
        final boolean created = id == null || "".equals(id.trim());
        FinancialReference financialReference;
        if (created) {
            financialReference = new FinancialReference();
            financialReference.setId(UUID.randomUUID().toString());
        } else {
            financialReference = financialReferenceRepository.findById(id);
            if (financialReference == null) {
                throw new IllegalStateException("Não foi possível atualizar os dados do tipo de lançamento.");
            }
        }

        final String description = financialReferenceVO.getDescription();
        if (description == null || "".equals(description.trim())) {
            throw new IllegalStateException("É obrigatório informar a descrição do tipo de lançamento");
        }
        financialReference.setDescription(description);

        financialReference.setCategory(Category.fromValue(financialReferenceVO.getCategory()));
        financialReference.setAssociatedWithCollaborator(financialReferenceVO.isAssociatedWithCollaborator());

        if (created) {
            financialReferenceRepository.create(financialReference);
        } else {
            financialReferenceRepository.update(financialReference);
        }
    }

    @RequestMapping(value = "/financial/reference/{id}", method = RequestMethod.DELETE)
    @Override
    public void removeFinancialReferenceById(
        @PathVariable("id") String id) {
        if (id == null || "".equals(id.trim())) {
            throw new IllegalArgumentException("Id do tipo de lançamento é inválido");
        }
        financialReferenceRepository.removeById(id);
    }

}
