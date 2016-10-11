package org.religion.umbanda.tad.model.financial;

import org.joda.time.DateTime;
import org.religion.umbanda.tad.model.*;
import org.religion.umbanda.tad.util.DateTimeUtils;

import java.text.NumberFormat;

public class FinancialEntryReceiptMailTemplate implements FinancialMailTemplate<FinancialReceipt> {

    private static final int MAX_ADDITIONAL_TEXT_LENGTH = 40;
    private static final String ADDITIONAL_TEXT_SUFFIX = "...";

    private final String subject;
    private final String body;

    FinancialEntryReceiptMailTemplate(String subject, String body) {
        this.subject = subject;
        this.body = body;
    }

    @Override
    public MailMessage createMailMessage(FinancialReceipt financialReceipt) {
        final Collaborator collaborator = financialReceipt.getCollaborator();
        final MailMessage mailMessage = new MailMessage();
        mailMessage.setSenderType(MailMessageSenderType.FINANCIAL);
        mailMessage.setType(MailMessageType.HTML);
        mailMessage.setTo(collaborator.getUserCredentials().getUserName());
        mailMessage.setSubject(subject);
        mailMessage.setContent(
            String.format(body,
                collaborator.getPerson().getName(),
                financialReceipt.getKey().value(),
                DateTimeUtils.toString(financialReceipt.getFinancialEntry().getEntryDate(), "dd/MMM/yyyy"),
                financialReceipt.getFinancialEntry().getType().getDescription(),
                getAdditionalText(financialReceipt.getFinancialEntry().getAdditionalText()),
                NumberFormat.getCurrencyInstance().format(financialReceipt.getFinancialEntry().getValue()),
                DateTimeUtils.toString(DateTime.now(), "dd/MMM/yyyy")));
        return mailMessage;
    }

    private String getAdditionalText(String additionalText) {
        String suffix = "";
        int size = additionalText.length();
        int end = MAX_ADDITIONAL_TEXT_LENGTH;
        if (size <= MAX_ADDITIONAL_TEXT_LENGTH) {
            end = size;
        } else {
            suffix = ADDITIONAL_TEXT_SUFFIX;
        }
        return additionalText.substring(0, end).concat(suffix);
    }
}
