package org.religion.umbanda.tad.service.impl;

import org.religion.umbanda.tad.model.financial.FinancialReceipt;
import org.religion.umbanda.tad.model.financial.FinancialReceiptKey;

public interface FinancialReceiptRepository {

    FinancialReceiptKey generateKey();
    void create(FinancialReceipt financialReceipt);
    void update(FinancialReceipt financialReceipt);

}
