package org.religion.umbanda.tad.service.impl;

import org.religion.umbanda.tad.model.Event;
import org.religion.umbanda.tad.service.EventRepository;
import org.religion.umbanda.tad.service.EventService;
import org.religion.umbanda.tad.service.vo.EventRequest;
import org.religion.umbanda.tad.service.vo.EventResponse;
import org.religion.umbanda.tad.util.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @RequestMapping(value = "/events/{year}", method = RequestMethod.GET, produces = "application/json")
    @Override
    public List<EventResponse> getEvents(
            @PathVariable("year") int year) {
        List<EventResponse> results = null;
        if (year > 0) {
            final List<Event> events = eventRepository.findEventByYear(year);
            results = new ArrayList<EventResponse>(events.size());
            for (Event event : events) {
                final EventResponse eventResponse = new EventResponse();
                eventResponse.setId(event.getId().toString());
                eventResponse.setTitle(event.getTitle());
                eventResponse.setNotes(event.getNotes());
                eventResponse.setDate(DateTimeUtils.toString(event.getDate()));
                eventResponse.setVisibility(event.getVisibility());
                results.add(eventResponse);
            }
        }
        return results;
    }

    @RequestMapping(value = "/event", method = RequestMethod.POST)
    @Override
    public void saveEvent(
            @RequestBody EventRequest request) {
        UUID id;
        final String eventId = request.getId();
        if (eventId != null && !"".equals(eventId)) {
            try {
                id = UUID.fromString(eventId);
                if (eventRepository.existsById(id)) {
                    eventRepository.updateEvent(doConvertEvent(id, request));
                }
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("ID do evento é inválido", ex);
            }
        } else {
            id = UUID.randomUUID();
            eventRepository.addEvent(doConvertEvent(id, request));
        }
    }

    private Event doConvertEvent(UUID id, EventRequest request) {
        final Event event = new Event();
        event.setId(id);
        event.setDate(DateTimeUtils.fromString(request.getDate()));
        event.setTitle(request.getTitle());
        event.setNotes(request.getNotes());
        event.setVisibility(request.getVisibility());
        return event;
    }

}
