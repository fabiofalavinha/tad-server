package org.religion.umbanda.tad.service;

import org.religion.umbanda.tad.model.financial.FinancialTarget;

import java.util.List;

public interface FinancialTargetRepository {

    List<FinancialTarget> findAll();

}
