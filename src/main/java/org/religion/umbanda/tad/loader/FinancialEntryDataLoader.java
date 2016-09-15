package org.religion.umbanda.tad.loader;

import org.religion.umbanda.tad.log.Log;
import org.religion.umbanda.tad.log.LogFactory;
import org.springframework.stereotype.Component;

@Component
class FinancialEntryDataLoader implements DataLoader {

    private static final Log log = LogFactory.createLog(FinancialEntryDataLoader.class);
    private static final String KEY = "financial.entry.file";

    @Override
    public boolean accept(String key) {
        return KEY.equals(key);
    }

    @Override
    public void load(String data) {
        // TODO parse file lines
    }
}
