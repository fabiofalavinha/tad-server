package org.religion.umbanda.tad.service;

import org.religion.umbanda.tad.model.financial.Balance;
import org.religion.umbanda.tad.service.vo.CloseFinancialEntryBalanceDTO;
import org.religion.umbanda.tad.service.vo.FinancialEntryDTO;
import org.religion.umbanda.tad.service.vo.FinancialReferenceVO;
import org.religion.umbanda.tad.service.vo.FinancialTargetVO;

import java.util.List;

public interface FinancialService {

    List<FinancialReferenceVO> getFinancialReferences();

    List<FinancialTargetVO> getFinancialTargets();

    List<FinancialEntryDTO> findFinancialEntriesBy(String fromDateString, String toDateString);

    Balance getCurrentBalance();

    void saveFinancialReference(FinancialReferenceVO financialReferenceVO);

    void removeFinancialReferenceById(String id);

    void saveFinancialEntry(FinancialEntryDTO financialEntryDTO);

    void removeFinancialEntry(String id);

    void closeFinancialEntry(CloseFinancialEntryBalanceDTO dto);

}
