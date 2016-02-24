package org.religion.umbanda.tad.model.financial;

public class CustomFinancialReferenceEntry implements FinancialReferenceEntry {

    private final String referenceName;

    public CustomFinancialReferenceEntry(String referenceName) {
        this.referenceName = referenceName;
    }

    @Override
    public String getId() {
        return referenceName;
    }

    @Override
    public String getName() {
        return referenceName;
    }
}
