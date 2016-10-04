package org.religion.umbanda.tad.model.financial;

import org.religion.umbanda.tad.model.Collaborator;
import org.religion.umbanda.tad.model.MailMessage;
import org.religion.umbanda.tad.model.MailSender;
import org.religion.umbanda.tad.model.MailTemplate;
import org.religion.umbanda.tad.util.DateTimeUtils;

import java.text.NumberFormat;

public class FinancialEntryReceiptMailTemplate implements MailTemplate<FinancialReceipt> {

    private static final int MAX_ADDITIONAL_TEXT_LENGTH = 40;
    private static final String ADDITIONAL_TEXT_SUFFIX = "...";

    private final String key;
    private final String subject;
    private final String body;

    private MailSender mailSender;

    FinancialEntryReceiptMailTemplate(String key, String subject, String body) {
        this.key = key;
        this.subject = subject;
        this.body = body;
    }

    public String getKey() {
        return key;
    }

    @Override
    public MailMessage createMailMessage(FinancialReceipt financialReceipt) {
        final Collaborator collaborator = financialReceipt.getCollaborator();
        final MailMessage mailMessage = new MailMessage();
        mailMessage.setFrom(mailSender.getFinancial());
        mailMessage.setTo(collaborator.getUserCredentials().getUserName());
        mailMessage.setSubject(subject);
        mailMessage.setContent(
            String.format(body,
                collaborator.getPerson().getName(),
                financialReceipt.getKey().value(),
                DateTimeUtils.toString(financialReceipt.getFinancialEntry().getEntryDate(), "dd/MMM/yyyy"),
                financialReceipt.getFinancialEntry().getType().getDescription(),
                NumberFormat.getCurrencyInstance().format(financialReceipt.getFinancialEntry().getValue()),
                getAdditionalText(financialReceipt.getFinancialEntry().getAdditionalText())));
        return mailMessage;
    }

    @Override
    public void setSender(MailSender mailSender) {
        this.mailSender = mailSender;
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
