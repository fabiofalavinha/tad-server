package org.religion.umbanda.tad.service;

import org.religion.umbanda.tad.service.vo.FinancialEntryDTO;
import org.religion.umbanda.tad.service.vo.FinancialReferenceVO;

import java.util.List;

public interface FinancialService {

    List<FinancialReferenceVO> getFinancialReferences();
    void saveFinancialReference(FinancialReferenceVO financialReferenceVO);
    void removeFinancialReferenceById(String id);

    void saveFinancialEntry(FinancialEntryDTO financialEntryDTO);

}
