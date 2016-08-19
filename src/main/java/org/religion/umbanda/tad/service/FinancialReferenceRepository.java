package org.religion.umbanda.tad.service;

import org.religion.umbanda.tad.model.financial.FinancialReference;

import java.util.List;

public interface FinancialReferenceRepository {

    List<FinancialReference> findAll();

    FinancialReference findById(String id);

    void create(FinancialReference financialReference);

    void update(FinancialReference financialReference);

    void removeById(String id);

}
