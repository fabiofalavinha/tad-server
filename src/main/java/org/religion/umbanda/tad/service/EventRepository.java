package org.religion.umbanda.tad.service;

import org.joda.time.DateTime;
import org.religion.umbanda.tad.model.Event;
import org.religion.umbanda.tad.model.VisibilityType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository {

    boolean existsById(UUID id);

    Optional<Event> findByTitleAndDate(String title, DateTime date);

    List<Event> findEventByYear(int year);

    List<Event> findEventByYear(int year, VisibilityType visibilityType);

    void addEvent(Event event);

    void updateEvent(Event event);

    void removeEventById(UUID id);

}
