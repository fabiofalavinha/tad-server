package org.religion.umbanda.tad.service;

import org.religion.umbanda.tad.model.Collaborator;

import java.util.List;
import java.util.UUID;

public interface CollaboratorRepository {

    List<Collaborator> findAll();

    Collaborator findById(UUID id);

    boolean existsById(UUID id);

    void removeById(UUID id);

    void addCollaborator(Collaborator newCollaborator);

    void updateCollaborator(Collaborator newCollaborator);

}
