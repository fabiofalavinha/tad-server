package org.religion.umbanda.tad.service;

import org.religion.umbanda.tad.service.vo.ConsecrationDTO;
import org.religion.umbanda.tad.service.vo.EventRequest;
import org.religion.umbanda.tad.service.vo.EventResponse;

import java.util.List;

public interface EventService {

    List<EventResponse> findEventsByYear(int year);
    List<EventResponse> findEventsByYear(int year, String visibility);
    List<ConsecrationDTO> findConsecrations();
    EventResponse saveEvent(EventRequest request);
    ConsecrationDTO findConsecrationByEvent(String eventId);
    void removeEvent(String id);
    void saveConsecration(String eventIdAsString, ConsecrationDTO consecration);

}
