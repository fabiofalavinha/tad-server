package org.religion.umbanda.tad.service;

import org.religion.umbanda.tad.model.Event;
import org.religion.umbanda.tad.service.vo.EventRequest;
import org.religion.umbanda.tad.service.vo.EventResponse;

import java.util.List;

public interface EventService {

    List<EventResponse> getEvents(int year);
    void saveEvent(EventRequest request);

}
