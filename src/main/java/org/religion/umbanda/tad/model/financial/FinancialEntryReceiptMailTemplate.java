package org.religion.umbanda.tad.model.financial;

import org.religion.umbanda.tad.model.Collaborator;
import org.religion.umbanda.tad.model.MailMessage;
import org.religion.umbanda.tad.model.MailTemplate;
import org.religion.umbanda.tad.util.DateTimeUtils;

import java.text.NumberFormat;

public class FinancialEntryReceiptMailTemplate implements MailTemplate<FinancialReceipt> {

    private final String key;
    private final String subject;
    private final String body;

    public FinancialEntryReceiptMailTemplate(String key, String subject, String body) {
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
        mailMessage.setTo(collaborator.getUserCredentials().getUserName());
        mailMessage.setSubject(subject);
        mailMessage.setText(
            String.format(body,
                collaborator.getPerson().getName(),
                financialReceipt.getKey().value(),
                DateTimeUtils.toString(financialReceipt.getFinancialEntry().getEntryDate(), "dd/MMM/yyyy"),
                NumberFormat.getCurrencyInstance().format(financialReceipt.getFinancialEntry().getValue()),
                financialReceipt.getFinancialEntry().getType().getDescription()));
        return mailMessage;
    }
}
