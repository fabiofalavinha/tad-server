package org.religion.umbanda.tad.service;

import org.religion.umbanda.tad.service.vo.EventRequest;
import org.religion.umbanda.tad.service.vo.EventResponse;

import java.util.List;

public interface EventService {

    List<EventResponse> findEventsByYear(int year);
    List<EventResponse> findEventsByYear(int year, String visibility);
    void saveEvent(EventRequest request);
    void removeEvent(String id);

}
