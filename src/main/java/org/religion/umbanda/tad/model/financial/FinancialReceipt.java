package org.religion.umbanda.tad.model.financial;

import org.joda.time.DateTime;
import org.religion.umbanda.tad.model.Collaborator;

public class FinancialReceipt {

    private FinancialReceiptKey key;
    private DateTime created;
    private DateTime sent;
    private Collaborator collaborator;
    private FinancialReceiptStatus status;
    private FinancialEntry financialEntry;

    public FinancialReceipt() {
        created = DateTime.now();
        status = FinancialReceiptStatus.CREATED;
    }

    public FinancialReceiptKey getKey() {
        return key;
    }

    public void setKey(FinancialReceiptKey key) {
        this.key = key;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    public DateTime getSent() {
        return sent;
    }

    public void setSent(DateTime sent) {
        this.sent = sent;
    }

    public FinancialEntry getFinancialEntry() {
        return financialEntry;
    }

    public void setFinancialEntry(FinancialEntry financialEntry) {
        this.financialEntry = financialEntry;
    }

    public FinancialReceiptStatus getStatus() {
        return status;
    }

    public void setStatus(FinancialReceiptStatus status) {
        this.status = status;
    }

    public Collaborator getCollaborator() {
        return collaborator;
    }

    public void setCollaborator(Collaborator collaborator) {
        this.collaborator = collaborator;
    }
}
