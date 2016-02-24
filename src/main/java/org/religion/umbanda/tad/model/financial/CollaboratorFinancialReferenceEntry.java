package org.religion.umbanda.tad.model.financial;

import org.religion.umbanda.tad.model.Collaborator;

public class CollaboratorFinancialReferenceEntry implements FinancialReferenceEntry {

    private final Collaborator collaborator;

    public CollaboratorFinancialReferenceEntry(Collaborator collaborator) {
        this.collaborator = collaborator;
    }

    @Override
    public String getId() {
        return collaborator.getPerson().getId().toString();
    }

    @Override
    public String getName() {
        return collaborator.getPerson().getName();
    }
}
