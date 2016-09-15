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
    private MailTemplateFactory mailTemplateFactory;

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
            if (lastCloseableBalanceFinancialEntry != null) {
                Balance lastBalance = lastCloseableBalanceFinancialEntry.getBalance();
                final List<FinancialEntry> entries = financialEntryRepository.findOpenedEntries();
                for (FinancialEntry oldEntry : entries) {
                    lastBalance = lastBalance.calculate(oldEntry.getValue(), oldEntry.getType().getCategory());
                    oldEntry.setBalance(lastBalance);
                    log.info("Updating financial entry balance [id=%s] with new balance [v=%f]...", oldEntry.getId(), lastBalance.getValue().doubleValue());
                    financialEntryRepository.update(oldEntry);
                }
                log.info("Updating financial total balance [v=%f]...", lastBalance.getValue().doubleValue());
                financialBalanceRepository.update(lastBalance);
            } else {
                Balance balance = new Balance(0);
                final List<FinancialEntry> entries = financialEntryRepository.findOpenedEntries();
                for (FinancialEntry oldEntry : entries) {
                    balance = balance.calculate(oldEntry.getValue(), oldEntry.getType().getCategory());
                    oldEntry.setBalance(balance);
                    log.info("Updating financial entry balance [id=%s] with new balance [v=%f]...", oldEntry.getId(), balance.getValue().doubleValue());
                    financialEntryRepository.update(oldEntry);
                }
                log.info("Updating financial total balance [v=%f]...", balance.getValue().doubleValue());
                financialBalanceRepository.update(balance);
            }
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
        DateTime closedDate = null;
        Balance balance = null;
        if (lastCloseableBalanceFinancialEntry != null) {
            log.info("Last closeable financial entry balance was retrieved");
            closedDate = lastCloseableBalanceFinancialEntry.getClosedDate();
            balance = lastCloseableBalanceFinancialEntry.getBalance();
        } else {
            log.info("Last closeable financial entry balance was NOT found. Retrieving first opened financial entry...");
            FinancialEntry financialEntry = financialEntryRepository.getFirstOpenedFinancialEntry();
            if (financialEntry != null) {
                log.info("First opened financial entry was retrieved");
                closedDate = financialEntry.getEntryDate();
                balance = new Balance(0);
            }
        }
        if (closedDate != null) {
            log.info("Retrieving opened financial entries...");
            final List<FinancialEntry> openedEntries = financialEntryRepository.findOpenedEntries();
            if (!openedEntries.isEmpty()) {
                log.info("Retrieved [%d] opened financial entries", openedEntries.size());
                final CloseableBalanceFinancialEntry newCloseableBalanceFinancialEntry = new CloseableBalanceFinancialEntry();
                newCloseableBalanceFinancialEntry.setId(UUID.randomUUID());
                newCloseableBalanceFinancialEntry.setClosedDate(DateTime.now());
                UserCredentials closedByUser = userCredentialsRepository.findById(UUID.fromString(userId));
                newCloseableBalanceFinancialEntry.setClosedBy(closedByUser);
                for (FinancialEntry entry : openedEntries) {
                    balance = balance.calculate(entry.getValue(), entry.getType().getCategory());
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
        } else {
            log.warn("No financial entry is available for closing");
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

        final FinancialTarget target = financialEntry.getTarget();
        if (target.getType() == FinancialTargetType.OTHER) {
            throw new IllegalStateException(String.format("Não é possível enviar recibo para lançamentos que não são origem de pessoas (colaboradores / não colaboradores) [id=%s]", id));
        }

        final Collaborator collaborator = collaboratorRepository.findById(target.getIdAsUUID());
        if (collaborator == null) {
            throw new IllegalStateException(String.format("Colaborador não foi encontrado [id=%s]", target.getId()));
        }

        final FinancialReceipt newFinancialReceipt = new FinancialReceipt();
        newFinancialReceipt.setKey(financialReceiptRepository.generateKey());
        newFinancialReceipt.setFinancialEntry(financialEntry);
        newFinancialReceipt.setCollaborator(collaborator);
        financialReceiptRepository.create(newFinancialReceipt);

        try {
            final MailTemplate<FinancialReceipt> mailTemplate = mailTemplateFactory.getTemplate(FinancialEntryReceiptMailTemplateConfiguration.KEY);
            final MailMessage mailMessage = mailTemplate.createMailMessage(newFinancialReceipt);
            mailService.send(mailMessage);

            newFinancialReceipt.setSent(DateTime.now());
            newFinancialReceipt.setStatus(FinancialReceiptStatus.SENT);
            financialReceiptRepository.update(newFinancialReceipt);
        } catch (Exception ex) {
            log.error("Error sending financial receipt mail: %s", ExceptionUtils.getMessage(ex));
            newFinancialReceipt.setStatus(FinancialReceiptStatus.ERROR);
            financialReceiptRepository.update(newFinancialReceipt);
        }

        final FinancialReceiptResultVO result = new FinancialReceiptResultVO();
        result.setId(newFinancialReceipt.getKey().value());
        result.setCreated(DateTimeUtils.toString(newFinancialReceipt.getCreated()));
        final DateTime receiptSentDate = newFinancialReceipt.getSent();
        result.setEntry(parseFinancialEntry(newFinancialReceipt.getFinancialEntry()));
        if (receiptSentDate != null) {
            result.setSent(DateTimeUtils.toString(receiptSentDate));
        }
        result.setStatus(newFinancialReceipt.getStatus().name());
        result.setTarget(CollaboratorParserUtils.parse(collaborator));

        return result;
    }
}
