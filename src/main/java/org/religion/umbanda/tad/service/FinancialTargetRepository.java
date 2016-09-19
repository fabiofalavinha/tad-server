package org.religion.umbanda.tad.service;

import org.religion.umbanda.tad.model.financial.FinancialTarget;

import java.util.List;
import java.util.Optional;

public interface FinancialTargetRepository {

    List<FinancialTarget> findAll();

    FinancialTarget findById(String id);

    Optional<FinancialTarget> findByName(String targetName);

    void create(FinancialTarget target);

}
