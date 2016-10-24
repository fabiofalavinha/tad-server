package org.religion.umbanda.tad.service;

import org.religion.umbanda.tad.model.Consecration;

import java.util.List;
import java.util.UUID;

public interface ConsecrationRepository {

    List<Consecration> findAll();
    Consecration findById(UUID id);
    Consecration findByEventId(UUID id);
    void save(Consecration consecration);
    void update(Consecration consecration);
    void removeByEventId(UUID eventId);

}
