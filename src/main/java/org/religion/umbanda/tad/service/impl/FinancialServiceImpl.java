package org.religion.umbanda.tad.service.impl;

import org.joda.time.DateTime;
import org.religion.umbanda.tad.model.financial.*;
import org.religion.umbanda.tad.service.*;
import org.religion.umbanda.tad.service.vo.FinancialEntryDTO;
import org.religion.umbanda.tad.service.vo.FinancialReferenceVO;
import org.religion.umbanda.tad.service.vo.FinancialTargetVO;
import org.religion.umbanda.tad.util.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
    public List<FinancialTargetVO> getFinancialTargets() {
        final List<FinancialTarget> targets = financialTargetRepository.findAll();
        final List<FinancialTargetVO> list = new ArrayList<>();
        for (FinancialTarget target : targets) {
            final FinancialTargetVO vo = new FinancialTargetVO();
            vo.setId(target.getId());
            vo.setName(target.getName());
            vo.setType(target.getType().value());
            list.add(vo);
        }
        return list;
    }

    @RequestMapping(value = "/financial/balance", method = RequestMethod.GET)
    @Override
    public Balance getCurrentBalance() {
        return financialBalanceRepository.getBalance();
    }

    @RequestMapping(value = "/financial/entries/{from}/{to}", method = RequestMethod.GET)
    @Override
    public List<FinancialEntryDTO> findFinancialEntriesBy(@PathVariable("from") String fromDateString, @PathVariable("to") String toDateString) {
        DateTime fromDate;
        DateTime toDate;
        try {
            fromDate = DateTimeUtils.fromString(fromDateString, "yyyy-MM-dd");
            toDate = DateTimeUtils.fromString(toDateString, "yyyy-MM-dd");
        } catch (Exception ex) {
            throw new IllegalStateException("Datas para consulta de lançamentos financeiros tem o formato inválido. Formato esperado: yyyy-MM-dd", ex);
        }
        final List<FinancialEntry> list = financialEntryRepository.findBy(fromDate, toDate);
        final List<FinancialEntryDTO> responseList = new ArrayList<>();
        for (FinancialEntry financialEntry : list) {
            final FinancialEntryDTO dto = new FinancialEntryDTO();
            dto.setId(financialEntry.getId());
            dto.setAdditionalText(financialEntry.getAdditionalText());
            dto.setBalance(financialEntry.getBalance());
            dto.setDate(DateTimeUtils.toString(financialEntry.getEntryDate(), "yyyy-MM-dd"));
            dto.setPreviewBalance(financialEntry.getPreviewBalance());
            dto.setValue(financialEntry.getValue());
            final FinancialTargetVO targetVO = new FinancialTargetVO();
            targetVO.setId(financialEntry.getTarget().getId());
            targetVO.setName(financialEntry.getTarget().getName());
            targetVO.setType(financialEntry.getTarget().getType().value());
            dto.setTarget(targetVO);
            dto.setType(convertFinancialReference(financialEntry.getType()));
            responseList.add(dto);
        }
        return responseList;
    }

    @RequestMapping(value = "/financial/entry", method = RequestMethod.POST)
    @Override
    public void saveFinancialEntry(@RequestBody FinancialEntryDTO financialEntryDTO) {
        boolean newEntry = false;
        BigDecimal oldEntryValue = null;

        FinancialEntry entry = financialEntryRepository.findById(financialEntryDTO.getId());
        if (entry == null) {
            newEntry = true;
            entry = new FinancialEntry();
            entry.setId(financialEntryDTO.getId());
        } else {
            oldEntryValue = entry.getValue();
        }

        entry.setEntryDate(DateTimeUtils.fromString(financialEntryDTO.getDate(), "yyyy-MM-dd"));
        entry.setAdditionalText(financialEntryDTO.getAdditionalText());
        entry.setValue(financialEntryDTO.getValue());
        entry.setBalance(financialEntryDTO.getBalance());

        final FinancialTargetVO targetVO = financialEntryDTO.getTarget();
        FinancialTarget target = financialTargetRepository.findById(targetVO.getId());
        if (target == null) {
            target = new FinancialTarget();
            target.setId(targetVO.getId());
            target.setName(targetVO.getName());
            target.setType(FinancialTargetType.fromValue(targetVO.getType()));
            financialTargetRepository.create(target);
        }
        entry.setTarget(target);

        entry.setType(financialReferenceRepository.findById(financialEntryDTO.getType().getId()));

        boolean updateBalance = true;
        if (newEntry) {
            financialEntryRepository.create(entry);
        } else {
            if (oldEntryValue.equals(entry.getValue())) {
                updateBalance = false;
            }
            financialEntryRepository.update(entry);

            // TODO
            // - if old entry value was changed, update every entry from this date
        }

        if (updateBalance) {
            final Balance currentBalance = financialBalanceRepository.getBalance();
            final Balance newBalance = currentBalance.calculate(entry.getValue(), entry.getType().getCategory());
            financialBalanceRepository.update(newBalance);
        }
    }
}
