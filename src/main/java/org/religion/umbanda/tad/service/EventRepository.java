package org.religion.umbanda.tad.service;

import org.religion.umbanda.tad.model.Event;

import java.util.List;
import java.util.UUID;

public interface EventRepository {

    boolean existsById(UUID id);
    List<Event> findEventByYear(int year);
    void addEvent(Event event);
    void updateEvent(Event event);
    void removeEventById(UUID id);

}
