package org.religion.umbanda.tad.model;

import java.util.List;
import java.util.UUID;

public class Consecration {

    private UUID id;
    private Event event;
    private CommunicationMessage message;
    private List<Element> elements;

    public Consecration() {
        id = UUID.randomUUID();
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public CommunicationMessage getMessage() {
        return message;
    }

    public void setMessage(CommunicationMessage message) {
        this.message = message;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
