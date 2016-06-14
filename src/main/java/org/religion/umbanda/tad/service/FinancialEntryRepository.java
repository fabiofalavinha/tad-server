package org.religion.umbanda.tad.service;

import org.joda.time.DateTime;
import org.religion.umbanda.tad.model.financial.FinancialEntry;

import java.util.List;

public interface FinancialEntryRepository {

    List<FinancialEntry> findBy(DateTime from, DateTime to);

}
