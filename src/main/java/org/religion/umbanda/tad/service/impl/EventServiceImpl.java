package org.religion.umbanda.tad.service.impl;

import org.joda.time.DateTime;
import org.religion.umbanda.tad.log.Log;
import org.religion.umbanda.tad.log.LogFactory;
import org.religion.umbanda.tad.model.*;
import org.religion.umbanda.tad.service.CollaboratorRepository;
import org.religion.umbanda.tad.service.ConsecrationRepository;
import org.religion.umbanda.tad.service.EventRepository;
import org.religion.umbanda.tad.service.EventService;
import org.religion.umbanda.tad.service.vo.ConsecrationDTO;
import org.religion.umbanda.tad.service.vo.ElementDTO;
import org.religion.umbanda.tad.service.vo.EventRequest;
import org.religion.umbanda.tad.service.vo.EventResponse;
import org.religion.umbanda.tad.util.DateTimeUtils;
import org.religion.umbanda.tad.util.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class EventServiceImpl implements EventService {

    private static final Log log = LogFactory.createLog(EventServiceImpl.class);

    private final EventRepository eventRepository;
    private final ConsecrationRepository consecrationRepository;
    private final CollaboratorRepository collaboratorRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, ConsecrationRepository consecrationRepository, CollaboratorRepository collaboratorRepository) {
        this.eventRepository = eventRepository;
        this.consecrationRepository = consecrationRepository;
        this.collaboratorRepository = collaboratorRepository;
    }

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
        return events.stream().map(this::convertEventResponse).collect(Collectors.toList());
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
            eventResponse.setCategory(event.getEventCategory().getValue());
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

        final Event event = eventRepository.findById(id);
        if (event != null) {
            eventRepository.removeEventById(id);
            consecrationRepository.removeByEventId(event.getId());
        }
    }

    @RequestMapping(value = "/event", method = RequestMethod.POST)
    @Override
    public EventResponse saveEvent(
            @RequestBody EventRequest request) {
        Event event = null;
        UUID id;
        final String eventId = request.getId();
        log.info("====> EVENT ID => %s", eventId);
        if (eventId != null && !"".equals(eventId)) {
            try {
                id = UUID.fromString(eventId);
                if (eventRepository.existsById(id)) {
                    event = doConvertEvent(id, request);
                    eventRepository.updateEvent(event);
                }
            } catch (IllegalArgumentException ex) {
                log.exception(ex, "Error updating event");
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
        event.setEventCategory(EventCategory.fromValue(request.getCategory()));
        return event;
    }

    @RequestMapping(value = "/event/{eventId}/consecration", method = RequestMethod.POST)
    @Override
    public void saveConsecration(
        @PathVariable("eventId") String eventIdAsString,
        @RequestBody ConsecrationDTO consecrationRequest) {
        UUID consecrationId;
        try {
            consecrationId = IdUtils.fromString(consecrationRequest.getId());
        } catch (IllegalArgumentException ex) {
            consecrationId = UUID.randomUUID();
        }

        Consecration consecration = consecrationRepository.findById(consecrationId);
        if (consecration == null) {
            consecration = new Consecration();
            consecration.setId(consecrationId);
        }

        final UUID eventId = IdUtils.fromString(eventIdAsString);
        final Event event = eventRepository.findById(eventId);
        if (event == null) {
            throw new IllegalArgumentException(String.format("Não foi possível encontrar o evento [%s]", eventId));
        }
        consecration.setEvent(event);

        consecration.setMessage(new CommunicationMessage(consecrationRequest.getCommunicationMessage()));

        final List<ElementDTO> elements = consecrationRequest.getElements();
        if (elements != null && !elements.isEmpty()) {
            consecration.setElements(elements.stream().map(e -> {
                final Element element = new Element(e.getName());

                UUID elementId;
                try {
                    elementId = IdUtils.fromString(e.getId());
                } catch (IllegalArgumentException ex) {
                    elementId = UUID.randomUUID();
                }
                element.setId(elementId);
                element.setUnit(e.getUnit());
                element.setQuantity(e.getQuantity());
                element.setPrimary(collaboratorRepository.findById(IdUtils.fromString(e.getPrimaryCollaboratorId())));
                element.setSecondary(collaboratorRepository.findById(IdUtils.fromString(e.getSecondaryCollaboratorId())));

                return element;
            }).collect(Collectors.toList()));
        }

        consecrationRepository.save(consecration);
    }

    @RequestMapping(value = "/event/{eventId}/consecration", method = RequestMethod.GET)
    @Override
    public ConsecrationDTO findConsecrationByEvent(
            @PathVariable("eventId") String eventId) {
        final Consecration consecration = consecrationRepository.findByEventId(IdUtils.fromString(eventId));
        if (consecration != null) {
            return convertConsecration(consecration);
        }
        return null;
    }

    private ConsecrationDTO convertConsecration(Consecration consecration) {
        final ConsecrationDTO dto = new ConsecrationDTO();
        dto.setId(consecration.getId().toString());
        dto.setCommunicationMessage(consecration.getMessage().getContent());
        dto.setEvent(convertEventResponse(consecration.getEvent()));
        dto.setElements(consecration.getElements().stream().map(e -> {
            ElementDTO elementDTO = new ElementDTO();
            elementDTO.setId(e.getId().toString());
            elementDTO.setName(e.getName());
            elementDTO.setQuantity(e.getQuantity());
            elementDTO.setUnit(e.getUnit());
            elementDTO.setPrimaryCollaboratorId(e.getPrimary().getPerson().getId().toString());
            elementDTO.setSecondaryCollaboratorId(e.getSecondary().getPerson().getId().toString());
            return elementDTO;
        }).collect(Collectors.toList()));
        return dto;
    }

    @RequestMapping(value = "/event/consecrations")
    @Override
    public List<ConsecrationDTO> findConsecrations() {
        return consecrationRepository.findAll().stream().map(this::convertConsecration).collect(Collectors.toList());
    }
}
