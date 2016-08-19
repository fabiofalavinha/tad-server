package org.religion.umbanda.tad.service;

import org.religion.umbanda.tad.model.financial.CloseableBalanceFinancialEntry;

public interface CloseableBalanceFinancialEntryRepository {

    void create(CloseableBalanceFinancialEntry closeableBalanceFinancialEntry);

    CloseableBalanceFinancialEntry getLastCloseableBalanceFinancialEntry();
}
