package org.religion.umbanda.tad.service.impl;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.DateTime;
import org.religion.umbanda.tad.log.Log;
import org.religion.umbanda.tad.log.LogFactory;
import org.religion.umbanda.tad.model.*;
import org.religion.umbanda.tad.model.financial.*;
import org.religion.umbanda.tad.service.*;
import org.religion.umbanda.tad.service.vo.*;
import org.religion.umbanda.tad.util.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class FinancialServiceImpl implements FinancialService {

    private static final Log log = LogFactory.createLog(FinancialServiceImpl.class);

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
    private CollaboratorRepository collaboratorRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private FinancialEntryReceiptMailTemplate financialEntryReceiptMailTemplate;

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    @Autowired
    private FinancialReceiptRepository financialReceiptRepository;

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
            final FinancialEntryDTO dto = parseFinancialEntry(financialEntry);
            responseList.add(dto);
        }
        return responseList;
    }

    private FinancialEntryDTO parseFinancialEntry(FinancialEntry financialEntry) {
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
        final FinancialReceiptInfoDTO financialReceiptInfoDTO = new FinancialReceiptInfoDTO();
        FinancialReceiptInfo financialReceiptInfo = financialEntry.getFinancialReceiptInfo();
        if (financialReceiptInfo != null) {
            financialReceiptInfoDTO.setNumber(financialReceiptInfo.getNumber());
            financialReceiptInfoDTO.setSent(DateTimeUtils.toString(financialReceiptInfo.getSent(), "yyyy-MM-dd"));
        }
        dto.setFinancialReceipt(financialReceiptInfoDTO);
        final CloseableFinancialEntry closeableFinancialEntry = financialEntry.getCloseableFinancialEntry();
        if (closeableFinancialEntry != null) {
            final CloseableFinancialEntryDTO closeableFinancialEntryDTO = new CloseableFinancialEntryDTO();
            closeableFinancialEntryDTO.setClosedDate(DateTimeUtils.toString(closeableFinancialEntry.getClosedDate(), "yyyy-MM-dd"));
            final UserCredentialsVO closedByUser = new UserCredentialsVO();
            closedByUser.setId(closeableFinancialEntry.getClosedBy().getId().toString());
            closedByUser.setUserName(closeableFinancialEntry.getClosedBy().getUserName());
            closedByUser.setName(closedByUser.getUserName());
            closedByUser.setUserRole(closeableFinancialEntry.getClosedBy().getUserRole());
            closeableFinancialEntryDTO.setClosedBy(closedByUser);
            dto.setCloseableFinancialEntry(closeableFinancialEntryDTO);
        }
        return dto;
    }

    @RequestMapping(value = "/financial/entry", method = RequestMethod.POST)
    @Override
    public void saveFinancialEntry(@RequestBody FinancialEntryDTO financialEntryDTO) {
        FinancialEntry entry = financialEntryRepository.findById(financialEntryDTO.getId());
        if (entry != null) {
            if (entry.isClosed()) {
                throw new IllegalStateException("Não é possível alterar um lançamento fechado!");
            }
            entry.setAdditionalText(financialEntryDTO.getAdditionalText());
            final BigDecimal oldValue = entry.getValue();
            entry.setValue(financialEntryDTO.getValue());
            final boolean valueChanged = !oldValue.equals(entry.getValue());
            if (valueChanged) {
                entry.setBalance(financialEntryDTO.getBalance());
            }
            financialEntryRepository.update(entry);
            if (valueChanged) {
                Balance currentBalance = financialBalanceRepository.getBalance();
                currentBalance = currentBalance.rollback(oldValue, entry.getType().getCategory());
                final Balance newBalance = currentBalance.calculate(entry.getValue(), entry.getType().getCategory());
                financialBalanceRepository.update(newBalance);
            }
        } else {
            log.info("Adding new financial entry [date=%s, value=%f]...", financialEntryDTO.getDate(), financialEntryDTO.getValue().doubleValue());
            entry = new FinancialEntry();
            entry.setId(financialEntryDTO.getId());
            entry.setEntryDate(DateTimeUtils.fromString(financialEntryDTO.getDate(), "yyyy-MM-dd"));
            entry.setAdditionalText(financialEntryDTO.getAdditionalText());
            entry.setValue(financialEntryDTO.getValue());
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
            Balance currentBalance = financialBalanceRepository.getBalance();
            currentBalance = currentBalance.calculate(entry.getValue(), entry.getType().getCategory());
            entry.setBalance(currentBalance);
            financialEntryRepository.create(entry);
            financialBalanceRepository.update(entry.getBalance());
        }
    }

    @RequestMapping(value = "/financial/entry/{id}", method = RequestMethod.DELETE)
    @Override
    public void removeFinancialEntry(@PathVariable("id") String id) {
        log.info("Requesting to remove financial entry [id=%s]...", id);
        final FinancialEntry entry = financialEntryRepository.findById(id);
        if (entry != null && entry.isOpened()) {
            log.info("Removing financial entry [id=%s]...", id);
            financialEntryRepository.remove(entry.getId());
            log.info("Re-calculating financial balance...");
            CloseableBalanceFinancialEntry lastCloseableBalanceFinancialEntry = closeableBalanceFinancialEntryRepository.getLastCloseableBalanceFinancialEntry();
            Balance balance = new Balance();
            if (lastCloseableBalanceFinancialEntry != null) {
                balance = lastCloseableBalanceFinancialEntry.getBalance();
            }
            final List<FinancialEntry> entries = financialEntryRepository.findOpenedEntries();
            for (FinancialEntry oldEntry : entries) {
                balance = balance.calculate(oldEntry.getValue(), oldEntry.getType().getCategory());
                oldEntry.setBalance(balance);
                log.info("Updating financial entry balance [id=%s] with new balance [v=%f]...", oldEntry.getId(), balance.getValue().doubleValue());
                financialEntryRepository.update(oldEntry);
            }
            log.info("Updating financial total balance [v=%f]...", balance.getValue().doubleValue());
            financialBalanceRepository.update(balance);
        } else {
            log.info("Financial entry [id=%s] can't be deleted because is closed", id);
        }
    }

    @RequestMapping(value = "/financial/close", method = RequestMethod.POST)
    @Override
    public void closeFinancialEntry(@RequestBody CloseFinancialEntryBalanceDTO dto) {
        log.info("Requesting to close financial entry balance...");
        final String userId = dto.getUserId();
        log.info("Retrieving last closeable financial entry balance...");
        CloseableBalanceFinancialEntry lastCloseableBalanceFinancialEntry = closeableBalanceFinancialEntryRepository.getLastCloseableBalanceFinancialEntry();

        Balance balance = new Balance(0);
        if (lastCloseableBalanceFinancialEntry != null) {
            log.info("Last closeable financial entry balance was retrieved");
            balance = lastCloseableBalanceFinancialEntry.getBalance();
        }
        log.info("Retrieving opened financial entries...");
        final List<FinancialEntry> openedEntries;
        final String closingDateString = dto.getClosingDate();
        if (closingDateString == null || closingDateString.isEmpty()) {
            openedEntries = financialEntryRepository.findOpenedEntries();
        } else {
            final DateTime closingDate = DateTimeUtils.fromString(closingDateString, "yyyy-MM-dd");
            openedEntries = financialEntryRepository.findOpenedEntriesUntil(closingDate);
        }
        if (!openedEntries.isEmpty()) {
            log.info("Retrieving first opened financial entry...");
            FinancialEntry financialEntry = openedEntries.iterator().next();
            DateTime closedDate = financialEntry.getEntryDate();
            log.info("Retrieved [%d] opened financial entries", openedEntries.size());
            final CloseableBalanceFinancialEntry newCloseableBalanceFinancialEntry = new CloseableBalanceFinancialEntry();
            newCloseableBalanceFinancialEntry.setId(UUID.randomUUID());
            newCloseableBalanceFinancialEntry.setClosedDate(closedDate);
            UserCredentials closedByUser = userCredentialsRepository.findById(UUID.fromString(userId));
            newCloseableBalanceFinancialEntry.setClosedBy(closedByUser);
            for (FinancialEntry entry : openedEntries) {
                balance = balance.calculate(entry.getValue(), entry.getType().getCategory());
                entry.setBalance(balance);
                CloseableFinancialEntry closeableFinancialEntry = new CloseableFinancialEntry();
                closeableFinancialEntry.setClosedDate(newCloseableBalanceFinancialEntry.getClosedDate());
                closeableFinancialEntry.setClosedBy(closedByUser);
                entry.setCloseableFinancialEntry(closeableFinancialEntry);
                entry.setStatus(FinancialEntryStatus.CLOSED);
                log.info("Update financial entry status [id=%s]", entry.getId());
                financialEntryRepository.update(entry);
            }
            newCloseableBalanceFinancialEntry.setBalance(balance);
            log.info("Creating closeable financial entry balance...");
            closeableBalanceFinancialEntryRepository.create(newCloseableBalanceFinancialEntry);
            log.info("Updating financial total balance...");
            financialBalanceRepository.update(balance);
        } else {
            log.info("No opened financial entries were found");
        }
    }

    @RequestMapping(value = "/financial/close/last", method = RequestMethod.GET)
    @Override
    public CloseableFinancialEntryDTO getLastCloseableFinancialEntry() {
        final CloseableBalanceFinancialEntry closeableFinancialEntry = closeableBalanceFinancialEntryRepository.getLastCloseableBalanceFinancialEntry();
        if (closeableFinancialEntry != null) {
            final CloseableFinancialEntryDTO closeableFinancialEntryDTO = new CloseableFinancialEntryDTO();
            closeableFinancialEntryDTO.setClosedDate(DateTimeUtils.toString(closeableFinancialEntry.getClosedDate(), "yyyy-MM-dd"));
            final UserCredentialsVO closedByUser = new UserCredentialsVO();
            closedByUser.setId(closeableFinancialEntry.getClosedBy().getId().toString());
            closedByUser.setUserName(closeableFinancialEntry.getClosedBy().getUserName());
            closedByUser.setName(closedByUser.getUserName());
            closedByUser.setUserRole(closeableFinancialEntry.getClosedBy().getUserRole());
            closeableFinancialEntryDTO.setClosedBy(closedByUser);
            return closeableFinancialEntryDTO;
        }
        return null;
    }

    @RequestMapping(value = "/financial/receipt/{id}", method = RequestMethod.POST)
    @Override
    public FinancialReceiptResultVO sendFinancialEntryReceipt(@PathVariable("id") String id) {
        final FinancialEntry financialEntry = financialEntryRepository.findById(id);
        if (financialEntry == null) {
            throw new IllegalArgumentException(String.format("Lançamento financeiro não foi encontrado [id=%s]", id));
        }

        FinancialReceipt financialReceipt = financialReceiptRepository.findByEntryId(financialEntry.getId());
        if (financialReceipt == null) {
            financialReceipt = new FinancialReceipt();
            financialReceipt.setFinancialEntry(financialEntry);

            final FinancialTarget target = financialEntry.getTarget();
            if (target.getType() == FinancialTargetType.OTHER) {
                throw new IllegalStateException(String.format("Não é possível enviar recibo para lançamentos que não são origem de pessoas (colaboradores / não colaboradores) [id=%s]", id));
            }

            final Collaborator collaborator = collaboratorRepository.findById(target.getIdAsUUID());
            if (collaborator == null) {
                throw new IllegalStateException(String.format("Colaborador não foi encontrado [id=%s]", target.getId()));
            }

            financialReceipt.setCollaborator(collaborator);
            financialReceipt.setKey(financialReceiptRepository.generateKey());
            financialReceiptRepository.create(financialReceipt);
        }

        try {
            mailService.send(financialEntryReceiptMailTemplate.createMailMessage(financialReceipt));

            financialReceipt.setSent(DateTime.now());
            financialReceipt.setStatus(FinancialReceiptStatus.SENT);
            financialReceiptRepository.update(financialReceipt);
        } catch (Exception ex) {
            log.error("Error sending financial receipt mail: %s", ExceptionUtils.getMessage(ex));
            financialReceipt.setStatus(FinancialReceiptStatus.ERROR);
            financialReceiptRepository.update(financialReceipt);
        }

        final FinancialReceiptResultVO result = new FinancialReceiptResultVO();
        result.setId(financialReceipt.getKey().value());
        result.setCreated(DateTimeUtils.toString(financialReceipt.getCreated()));
        final DateTime receiptSentDate = financialReceipt.getSent();
        result.setEntry(parseFinancialEntry(financialReceipt.getFinancialEntry()));
        if (receiptSentDate != null) {
            result.setSent(DateTimeUtils.toString(receiptSentDate));
        }
        result.setStatus(financialReceipt.getStatus().name());
        result.setTarget(CollaboratorParserUtils.parse(financialReceipt.getCollaborator()));

        return result;
    }

    @RequestMapping(value = "/financial/opened/last", method = RequestMethod.GET)
    @Override
    public FinancialEntryDTO getLastOpenedEntry() {
        final FinancialEntry entry = financialEntryRepository.getLastOpenedFinancialEntry();
        if (entry != null) {
            return parseFinancialEntry(entry);
        }
        return null;
    }

    @RequestMapping(value = "/financial/opened/first", method = RequestMethod.GET)
    @Override
    public FinancialEntryDTO getFirstOpenedEntry() {
        final FinancialEntry entry = financialEntryRepository.getFirstOpenedFinancialEntry();
        if (entry != null) {
            return parseFinancialEntry(entry);
        }
        return null;
    }
}
