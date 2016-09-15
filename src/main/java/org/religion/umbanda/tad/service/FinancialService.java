package org.religion.umbanda.tad.service;

import org.religion.umbanda.tad.model.financial.Balance;
import org.religion.umbanda.tad.service.vo.*;

import java.util.List;

public interface FinancialService {

    List<FinancialReferenceVO> getFinancialReferences();

    List<FinancialTargetVO> getFinancialTargets();

    List<FinancialEntryDTO> findFinancialEntriesBy(String fromDateString, String toDateString);

    Balance getCurrentBalance();

    CloseableFinancialEntryDTO getLastCloseableFinancialEntry();

    FinancialReceiptResultVO sendFinancialEntryReceipt(String id);

    void saveFinancialReference(FinancialReferenceVO financialReferenceVO);

    void removeFinancialReferenceById(String id);

    void saveFinancialEntry(FinancialEntryDTO financialEntryDTO);

    void removeFinancialEntry(String id);

    void closeFinancialEntry(CloseFinancialEntryBalanceDTO dto);

}
