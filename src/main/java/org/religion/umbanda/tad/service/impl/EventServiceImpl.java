package org.religion.umbanda.tad.service.impl;

import org.joda.time.DateTime;
import org.religion.umbanda.tad.model.Event;
import org.religion.umbanda.tad.model.VisibilityType;
import org.religion.umbanda.tad.service.EventRepository;
import org.religion.umbanda.tad.service.EventService;
import org.religion.umbanda.tad.service.vo.EventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @RequestMapping(value = "/events/{year}", method = RequestMethod.GET, produces = "application/json")
    @Override
    public List<Event> getEvents(
            @PathVariable("year") int year) {
        return eventRepository.findEventByYear(year);
    }

    @RequestMapping(value = "/event", method = RequestMethod.POST, consumes =  "application/json")
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
        event.setDate(request.getDate());
        event.setTitle(request.getTitle());
        event.setNotes(request.getNotes());
        event.setVisibility(request.getVisibility());
        return event;
    }

}
