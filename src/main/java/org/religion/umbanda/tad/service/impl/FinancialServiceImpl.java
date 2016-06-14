package org.religion.umbanda.tad.service.impl;

import org.joda.time.DateTime;
import org.religion.umbanda.tad.model.financial.*;
import org.religion.umbanda.tad.service.*;
import org.religion.umbanda.tad.service.vo.FinancialEntryDTO;
import org.religion.umbanda.tad.service.vo.FinancialReferenceVO;
import org.religion.umbanda.tad.util.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class FinancialServiceImpl implements FinancialService {

    @Autowired
    private FinancialReferenceRepository financialReferenceRepository;

    @Autowired
    private FinancialTargetRepository financialTargetRepository;

    @Autowired
    private FinancialBalanceRepository financialBalanceRepository;

    @Autowired
    private FinancialEntryRepository financialEntryRepository;

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
    public void removeFinancialReferenceById(@PathVariable("id") String id) {
        if (id == null || "".equals(id.trim())) {
            throw new IllegalArgumentException("Id do tipo de lançamento é inválido");
        }
        financialReferenceRepository.removeById(id);
    }

    @RequestMapping(value = "/financial/targets", method = RequestMethod.GET)
    @Override
    public List<FinancialTarget> getFinancialTargets() {
        return financialTargetRepository.findAll();
    }

    @RequestMapping(value = "/financial/balance", method = RequestMethod.GET)
    @Override
    public Balance getCurrentBalance() {
        return financialBalanceRepository.getBalance();
    }

    @RequestMapping(value = "/financial/entries/{from}/{to}", method = RequestMethod.GET)
    @Override
    public List<FinancialEntryDTO> findFinancialEntriesBy(@PathVariable("from") String fromDateString, @PathVariable("to") String toDateString) {
        final DateTime fromDate = DateTimeUtils.fromString(fromDateString);
        final DateTime toDate = DateTimeUtils.fromString(toDateString);
        final List<FinancialEntry> list = financialEntryRepository.findBy(fromDate, toDate);
        final List<FinancialEntryDTO> responseList = new ArrayList<>();
        for (FinancialEntry financialEntry : list) {
            final FinancialEntryDTO dto = new FinancialEntryDTO();
            dto.setId(financialEntry.getId());
            dto.setAdditionalText(financialEntry.getAdditionalText());
            dto.setBalance(financialEntry.getBalance());
            dto.setDate(DateTimeUtils.toString(financialEntry.getEntryDate()));
            dto.setPreviewBalance(financialEntry.getPreviewBalance());
            dto.setValue(financialEntry.getValue());
            dto.setTarget(financialEntry.getTarget());
            dto.setType(convertFinancialReference(financialEntry.getType()));
            responseList.add(dto);
        }
        return responseList;
    }

    @RequestMapping(value = "/financial/entry", method = RequestMethod.POST)
    @Override
    public void saveFinancialEntry(@RequestBody FinancialEntryDTO financialEntryDTO) {
    }
}
