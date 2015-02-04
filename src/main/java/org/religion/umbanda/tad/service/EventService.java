package org.religion.umbanda.tad.service;

import org.religion.umbanda.tad.model.Event;
import org.religion.umbanda.tad.service.vo.EventRequest;

import java.util.List;

public interface EventService {

    List<Event> getEvents(int year);
    void saveEvent(EventRequest request);

}
