package org.religion.umbanda.tad.service.impl;

import org.joda.time.DateTime;
import org.religion.umbanda.tad.model.Event;
import org.religion.umbanda.tad.model.VisibilityType;
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

    @RequestMapping(value = "/events/{visibility}/{year}", method = RequestMethod.GET, produces = "application/json")
    @Override
    public List<EventResponse> findEventsByYear(
        @PathVariable("year") int year,
        @PathVariable("visibility") String visibility) {
        VisibilityType visibilityType;
        try {
            visibilityType = VisibilityType.valueOf(visibility);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Não é possível retornar os eventos", ex);
        }
        if (year > 0) {
            return doConvertEvents(eventRepository.findEventByYear(year, visibilityType));
        }
        return new ArrayList<>(0);
    }

    @RequestMapping(value = "/events/{year}", method = RequestMethod.GET, produces = "application/json")
    @Override
    public List<EventResponse> findEventsByYear(
        @PathVariable("year") int year) {
        if (year > 0) {
            return doConvertEvents(eventRepository.findEventByYear(year));
        }
        return new ArrayList<>(0);
    }

    private List<EventResponse> doConvertEvents(List<Event> events) {
        final List<EventResponse> eventResponseList = new ArrayList<>(events.size());
        for (Event event : events) {
            eventResponseList.add(convertEventResponse(event));
        }
        return eventResponseList;
    }

    private EventResponse convertEventResponse(Event event) {
        final EventResponse eventResponse = new EventResponse();
        if (event != null) {
            eventResponse.setId(event.getId().toString());
            eventResponse.setTitle(event.getTitle());
            eventResponse.setNotes(event.getNotes());
            eventResponse.setDate(DateTimeUtils.toString(event.getDate()));
            eventResponse.setVisibility(event.getVisibility().getValue());
            eventResponse.setBackColor(event.getBackColor());
            eventResponse.setFontColor(event.getFontColor());
        }
        return eventResponse;
    }

    @RequestMapping(value = "/event/{id}", method = RequestMethod.DELETE)
    @Override
    public void removeEvent(
        @PathVariable("id") String eventId) {
        UUID id;
        try {
            id = UUID.fromString(eventId);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Could not parse event id", ex);
        }
        eventRepository.removeEventById(id);
    }

    @RequestMapping(value = "/event", method = RequestMethod.POST)
    @Override
    public EventResponse saveEvent(
        @RequestBody EventRequest request) {
        Event event = null;
        UUID id;
        final String eventId = request.getId();
        if (eventId != null && !"".equals(eventId)) {
            try {
                id = UUID.fromString(eventId);
                if (eventRepository.existsById(id)) {
                    event = doConvertEvent(id, request);
                    eventRepository.updateEvent(event);
                }
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("ID do evento é inválido", ex);
            }
        } else {
            id = UUID.randomUUID();
            event = doConvertEvent(id, request);
            eventRepository.addEvent(event);
        }
        return convertEventResponse(event);
    }

    private Event doConvertEvent(UUID id, EventRequest request) {
        final Event event = new Event();
        event.setId(id);
        event.setDate(DateTime.parse(request.getDate()));
        event.setTitle(request.getTitle());
        event.setNotes(request.getNotes());
        event.setVisibility(VisibilityType.fromValue(request.getVisibility()));
        event.setFontColor(request.getFontColor());
        event.setBackColor(request.getBackColor());
        return event;
    }

}
