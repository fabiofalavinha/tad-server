package org.religion.umbanda.tad.service;

import org.religion.umbanda.tad.model.Consecration;

import java.util.List;

public interface ConsecrationRepository {

    List<Consecration> findAll();
    void save(Consecration consecration);
    void update(Consecration consecration);

}
