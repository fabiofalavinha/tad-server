package org.religion.umbanda.tad.service.vo;

import java.util.List;

public class ConsecrationDTO {

    private String id;
    private String communicationMessage;
    private List<ElementDTO> elements;
    private EventResponse event;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommunicationMessage() {
        return communicationMessage;
    }

    public void setCommunicationMessage(String communicationMessage) {
        this.communicationMessage = communicationMessage;
    }

    public List<ElementDTO> getElements() {
        return elements;
    }

    public void setElements(List<ElementDTO> elements) {
        this.elements = elements;
    }

    public void setEvent(EventResponse event) {
        this.event = event;
    }

    public EventResponse getEvent() {
        return event;
    }
}
