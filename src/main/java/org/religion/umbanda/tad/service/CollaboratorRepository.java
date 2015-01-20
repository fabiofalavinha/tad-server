package org.religion.umbanda.tad.service;

import org.religion.umbanda.tad.model.Collaborator;

import java.util.List;

public interface CollaboratorRepository {

    List<Collaborator> findAll();
    Collaborator findById(String id);
    void removeById(String id);
}
