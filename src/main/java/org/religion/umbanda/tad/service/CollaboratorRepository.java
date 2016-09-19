package org.religion.umbanda.tad.service;

import org.religion.umbanda.tad.model.Collaborator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CollaboratorRepository {

    List<Collaborator> findAll();

    Collaborator findById(UUID id);

    Optional<Collaborator> findByPersonName(String personName);

    boolean existsById(UUID id);

    void removeById(UUID id);

    void addCollaborator(Collaborator newCollaborator);

    void updateCollaborator(Collaborator newCollaborator);

}
