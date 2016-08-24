package org.religion.umbanda.tad.service.impl;

import org.joda.time.DateTime;
import org.religion.umbanda.tad.model.UserCredentials;
import org.religion.umbanda.tad.model.financial.*;
import org.religion.umbanda.tad.service.*;
import org.religion.umbanda.tad.service.vo.*;
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

    @Autowired
    private CloseableBalanceFinancialEntryRepository closeableBalanceFinancialEntryRepository;

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

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
            dto.setStatus(financialEntry.getStatus().getValue());
            final CloseableFinancialEntry closeableFinancialEntry = financialEntry.getCloseableFinancialEntry();
            if (closeableFinancialEntry != null) {
                final CloseableFinancialEntryDTO closeableFinancialEntryDTO = new CloseableFinancialEntryDTO();
                closeableFinancialEntryDTO.setClosedDate(closeableFinancialEntry.getClosedDate());
                final UserCredentialsVO closedByUser = new UserCredentialsVO();
                closedByUser.setId(closeableFinancialEntry.getClosedBy().getId().toString());
                closedByUser.setName(closeableFinancialEntry.getClosedBy().getPerson().getName());
                closedByUser.setUserName(closeableFinancialEntry.getClosedBy().getUserName());
                closedByUser.setUserRole(closeableFinancialEntry.getClosedBy().getUserRole());
                closeableFinancialEntryDTO.setUserCredentialsVO(closedByUser);
                dto.setCloseableFinancialEntry(closeableFinancialEntryDTO);
            }
            responseList.add(dto);
        }
        return responseList;
    }

    @RequestMapping(value = "/financial/entry", method = RequestMethod.POST)
    @Override
    public void saveFinancialEntry(@RequestBody FinancialEntryDTO financialEntryDTO) {
        FinancialEntry entry = financialEntryRepository.findById(financialEntryDTO.getId());
        if (entry != null && entry.isClosed()) {
            throw new IllegalStateException("Não é possível alterar um lançamento fechado!");
        }

        entry = new FinancialEntry();
        entry.setId(financialEntryDTO.getId());
        entry.setEntryDate(DateTimeUtils.fromString(financialEntryDTO.getDate(), "yyyy-MM-dd"));
        entry.setAdditionalText(financialEntryDTO.getAdditionalText());
        entry.setValue(financialEntryDTO.getValue());
        entry.setBalance(financialEntryDTO.getBalance());
        entry.setStatus(FinancialEntryStatus.OPEN);

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

        financialEntryRepository.create(entry);
        final Balance currentBalance = financialBalanceRepository.getBalance();
        final Balance newBalance = currentBalance.calculate(entry.getValue(), entry.getType().getCategory());
        financialBalanceRepository.update(newBalance);
    }

    @RequestMapping(value = "/financial/entry/{id}", method = RequestMethod.DELETE)
    @Override
    public void removeFinancialEntry(@PathVariable("id") String id) {
        final FinancialEntry entry = financialEntryRepository.findById(id);
        if (entry != null && entry.isOpened()) {
            financialEntryRepository.remove(entry.getId());
            final Balance entryBalance = entry.getBalance();
            Balance newInTimeBalance = entryBalance.rollback(entry.getValue(), entry.getType().getCategory());
            final List<FinancialEntry> entries = financialEntryRepository.findBy(entry.getEntryDate(), DateTime.now());
            for (FinancialEntry oldEntry : entries) {
                newInTimeBalance = newInTimeBalance.calculate(oldEntry.getValue(), oldEntry.getType().getCategory());
                oldEntry.setBalance(newInTimeBalance);
                financialEntryRepository.update(oldEntry);
            }
            financialBalanceRepository.update(newInTimeBalance);
        }
    }

    @RequestMapping(value = "/financial/close", method = RequestMethod.POST)
    @Override
    public void closeFinancialEntry(@RequestBody CloseFinancialEntryBalanceDTO dto) {
        final String userId = dto.getUserId();
        CloseableBalanceFinancialEntry lastCloseableBalanceFinancialEntry = closeableBalanceFinancialEntryRepository.getLastCloseableBalanceFinancialEntry();
        DateTime closedDate = null;
        Balance balance = null;
        if (lastCloseableBalanceFinancialEntry != null) {
            closedDate = lastCloseableBalanceFinancialEntry.getClosedDate();
            balance = lastCloseableBalanceFinancialEntry.getBalance();
        } else {
            FinancialEntry financialEntry = financialEntryRepository.getFirstFinancialEntry();
            if (financialEntry != null) {
                closedDate = financialEntry.getEntryDate();
                balance = new Balance(0);
            }
        }
        if (closedDate != null) {
            List<FinancialEntry> financialEntryList = financialEntryRepository.findBy(closedDate);
            CloseableBalanceFinancialEntry newCloseableBalanceFinancialEntry = new CloseableBalanceFinancialEntry();
            newCloseableBalanceFinancialEntry.setId(UUID.randomUUID());
            newCloseableBalanceFinancialEntry.setClosedDate(DateTime.now());
            UserCredentials closedByUser = userCredentialsRepository.findById(UUID.fromString(userId));
            newCloseableBalanceFinancialEntry.setClosedBy(closedByUser);
            for (FinancialEntry entry : financialEntryList) {
                balance = balance.calculate(entry.getValue(), entry.getType().getCategory());
                CloseableFinancialEntry closeableFinancialEntry = new CloseableFinancialEntry();
                closeableFinancialEntry.setClosedDate(newCloseableBalanceFinancialEntry.getClosedDate());
                closeableFinancialEntry.setClosedBy(closedByUser);
                entry.setCloseableFinancialEntry(closeableFinancialEntry);
                entry.setStatus(FinancialEntryStatus.CLOSED);
                financialEntryRepository.update(entry);
            }
            newCloseableBalanceFinancialEntry.setBalance(balance);
            closeableBalanceFinancialEntryRepository.create(newCloseableBalanceFinancialEntry);
            financialBalanceRepository.update(balance);
        }
    }
}
