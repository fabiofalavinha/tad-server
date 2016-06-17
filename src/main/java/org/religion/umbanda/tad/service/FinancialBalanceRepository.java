package org.religion.umbanda.tad.service;

import org.religion.umbanda.tad.model.financial.Balance;

public interface FinancialBalanceRepository {

    Balance getBalance();

    void update(Balance newBalance);

}
