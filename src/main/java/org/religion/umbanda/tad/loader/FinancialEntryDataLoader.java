package org.religion.umbanda.tad.loader;

import org.religion.umbanda.tad.log.Log;
import org.religion.umbanda.tad.log.LogFactory;
import org.religion.umbanda.tad.model.Collaborator;
import org.religion.umbanda.tad.model.financial.*;
import org.religion.umbanda.tad.service.*;
import org.religion.umbanda.tad.util.DateTimeUtils;
import org.religion.umbanda.tad.util.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Component
class FinancialEntryDataLoader implements DataLoader {

    private static final Log log = LogFactory.createLog(FinancialEntryDataLoader.class);
    private static final String KEY = "financial.entry.file";
    private static final String LINE_TOKEN_SEPARATOR = ";";
    private static final int HEADER_LINE = 1;

    @Autowired
    private FinancialEntryRepository financialEntryRepository;

    @Autowired
    private FinancialReferenceRepository financialReferenceRepository;

    @Autowired
    private CollaboratorRepository collaboratorRepository;

    @Autowired
    private FinancialTargetRepository financialTargetRepository;

    @Autowired
    private FinancialBalanceRepository financialBalanceRepository;

    @Override
    public boolean accept(String key) {
        return KEY.equals(key);
    }

    @Override
    public void load(String data) {
        try (Stream<String> stream = Files.lines(Paths.get(data), Charset.forName("Cp1252"))) {
            stream.skip(HEADER_LINE).forEach(l -> {
                try {
                    String[] lineTokens = l.split(LINE_TOKEN_SEPARATOR);

                    FinancialEntryToken entryToken = new FinancialEntryToken();
                    entryToken.setEntryDate(lineTokens[0].trim());
                    entryToken.setTargetName(lineTokens[1].trim());
                    entryToken.setReferenceTypeName(lineTokens[2].trim());
                    entryToken.setObservationText(lineTokens[3].trim());
                    entryToken.setValue(lineTokens[4].trim());
                    entryToken.setCategoryType(lineTokens[5].trim());
                    entryToken.setBalance(lineTokens[6].trim());
                    entryToken.setTargetType(lineTokens[7].trim());

                    FinancialEntry newFinancialEntry = new FinancialEntry();
                    newFinancialEntry.setStatus(FinancialEntryStatus.OPEN);
                    newFinancialEntry.setId(UUID.randomUUID().toString());
                    newFinancialEntry.setAdditionalText(entryToken.getObservationText());
                    newFinancialEntry.setEntryDate(DateTimeUtils.fromString(entryToken.getEntryDate(), "dd-MM-yy"));
                    newFinancialEntry.setValue(NumberUtils.parseNumber(entryToken.getValue()));
                    newFinancialEntry.setBalance(Balance.fromString(entryToken.getBalance()));

                    Optional<FinancialReference> financialReferenceOptional = financialReferenceRepository.findByDescription(entryToken.getReferenceTypeName());
                    if (financialReferenceOptional.isPresent()) {
                        newFinancialEntry.setType(financialReferenceOptional.get());
                    } else {
                        throw new IllegalStateException(String.format("Could not find financial reference [%s]", entryToken.getReferenceTypeName()));
                    }

                    String targetName = entryToken.getTargetName();
                    Optional<Collaborator> collaboratorOptional = collaboratorRepository.findByPersonName(targetName);
                    if (collaboratorOptional.isPresent()) {
                        Collaborator collaborator = collaboratorOptional.get();
                        String targetId = collaborator.getPerson().getId().toString();
                        FinancialTarget target = financialTargetRepository.findById(targetId);
                        if (target == null) {
                            target = new FinancialTarget();
                            target.setId(targetId);
                            target.setName(collaborator.getPerson().getName());
                            target.setType(FinancialTargetType.fromValue(Integer.parseInt(entryToken.getTargetType())));
                            financialTargetRepository.create(target);
                        }
                        newFinancialEntry.setTarget(target);
                    } else {
                        Optional<FinancialTarget> financialTargetOptional = financialTargetRepository.findByName(targetName);
                        FinancialTarget target;
                        if (!financialTargetOptional.isPresent()) {
                            target = new FinancialTarget();
                            target.setId(UUID.randomUUID().toString());
                            target.setName(targetName);
                            target.setType(FinancialTargetType.fromValue(Integer.parseInt(entryToken.getTargetType())));
                            financialTargetRepository.create(target);
                        } else {
                            target = financialTargetOptional.get();
                        }
                        newFinancialEntry.setTarget(target);
                    }

                    log.info("Creating financial entry [line=%s]...", l);
                    Balance currentBalance = financialBalanceRepository.getBalance();
                    currentBalance = currentBalance.calculate(newFinancialEntry.getValue(), newFinancialEntry.getType().getCategory());
                    newFinancialEntry.setBalance(currentBalance);
                    financialEntryRepository.create(newFinancialEntry);
                    financialBalanceRepository.update(currentBalance);
                } catch (IllegalStateException | IllegalArgumentException ex) {
                    log.exception(ex, "Error parsing financial entry [%s]", l);
                }
            });
        } catch (IOException e) {
            log.exception(e, "Error reading file [%s]", data);
        }
    }

    private class FinancialEntryToken {

        private String entryDate;
        private String targetName;
        private String targetType;
        private String referenceTypeName;
        private String observationText;
        private String value;
        private String categoryType;
        private String balance;

        public String getEntryDate() {
            return entryDate;
        }

        public void setEntryDate(String entryDate) {
            this.entryDate = entryDate;
        }

        public String getTargetName() {
            return targetName;
        }

        public void setTargetName(String targetName) {
            this.targetName = targetName;
        }

        public String getReferenceTypeName() {
            return referenceTypeName;
        }

        public void setReferenceTypeName(String referenceTypeName) {
            this.referenceTypeName = referenceTypeName;
        }

        public String getObservationText() {
            return observationText;
        }

        public void setObservationText(String observationText) {
            this.observationText = observationText;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCategoryType() {
            return categoryType;
        }

        public void setCategoryType(String categoryType) {
            this.categoryType = categoryType;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getTargetType() {
            return targetType;
        }

        public void setTargetType(String targetType) {
            this.targetType = targetType;
        }
    }
}
