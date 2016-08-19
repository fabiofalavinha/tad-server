package org.religion.umbanda.tad.service;

import org.joda.time.DateTime;
import org.religion.umbanda.tad.model.financial.FinancialEntry;

import java.util.List;

public interface FinancialEntryRepository {

    List<FinancialEntry> findBy(DateTime from, DateTime to);

    FinancialEntry findById(String id);

    FinancialEntry getFirstFinancialEntry();

    List<FinancialEntry> findBy(DateTime from);

    void create(FinancialEntry entry);

    void update(FinancialEntry entry);

    void remove(String id);

}
