package org.religion.umbanda.tad.service;

import org.religion.umbanda.tad.model.financial.FinancialReference;

import java.util.List;
import java.util.Optional;

public interface FinancialReferenceRepository {

    List<FinancialReference> findAll();

    FinancialReference findById(String id);

    Optional<FinancialReference> findByDescription(String description);

    void create(FinancialReference financialReference);

    void update(FinancialReference financialReference);

    void removeById(String id);

}
