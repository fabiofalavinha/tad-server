package org.religion.umbanda.tad.service.impl;

import org.religion.umbanda.tad.model.*;
import org.religion.umbanda.tad.service.CollaboratorRepository;
import org.religion.umbanda.tad.service.ConsecrationRepository;
import org.religion.umbanda.tad.service.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class ConsecrationRepositoryImpl implements ConsecrationRepository {

    private class ElementData {
        String primaryCollaboratorId;
        String secondaryCollaboratorId;
        Element wrapper;
    }

    private class ConsecrationData {
        Consecration wrapper;
        String eventId;
    }

    private final JdbcTemplate jdbcTemplate;
    private final EventRepository eventRepository;
    private final CollaboratorRepository collaboratorRepository;

    @Autowired
    public ConsecrationRepositoryImpl(JdbcTemplate jdbcTemplate, EventRepository eventRepository, CollaboratorRepository collaboratorRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.eventRepository = eventRepository;
        this.collaboratorRepository = collaboratorRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Consecration> findAll() {
        final List<ConsecrationData> consecrationsData = jdbcTemplate.query(
            "select * from Consecration", (resultSet, i) -> {
                final ConsecrationData consecrationData = new ConsecrationData();

                final Consecration consecration = new Consecration();
                consecration.setId(UUID.fromString(resultSet.getString("consecrationId")));
                consecration.setName(resultSet.getString("consecrationName"));
                final CommunicationMessage message = new CommunicationMessage();
                message.setContent(resultSet.getString("communication_message"));
                consecration.setMessage(message);

                consecrationData.wrapper = consecration;
                consecrationData.eventId = resultSet.getString("event_id");

                return consecrationData;
            });

        final List<Consecration> consecrations = new ArrayList<>();
        for (ConsecrationData consecrationData : consecrationsData) {
            final Consecration consecration = consecrationData.wrapper;

            final Event event = eventRepository.findById(UUID.fromString(consecrationData.eventId));
            consecration.setEvent(event);

            final List<ElementData> elementsData =
                jdbcTemplate.query("select * from Element where consecration_id=?", new Object[] { consecration.getId().toString() }, (resultSet, i) -> {
                    final ElementData elementData = new ElementData();
                    elementData.primaryCollaboratorId = resultSet.getString("primary_collaborator_id");
                    elementData.secondaryCollaboratorId = resultSet.getString("secondary_collaborator_id");

                    final Element element = new Element(resultSet.getString("name"));
                    element.setId(UUID.fromString(resultSet.getString("id")));
                    element.setQuantity(resultSet.getInt("quantity"));
                    element.setUnit(resultSet.getString("unit"));
                    elementData.wrapper = element;

                    return elementData;
                });

            final List<Element> elements = new ArrayList<>();
            for (ElementData elementData : elementsData) {
                final Element element = elementData.wrapper;
                if (elementData.primaryCollaboratorId != null) {
                    UUID id = UUID.fromString(elementData.primaryCollaboratorId);
                    Collaborator collaborator = collaboratorRepository.findById(id);
                    element.setPrimary(collaborator);
                }
                if (elementData.secondaryCollaboratorId != null) {
                    UUID id = UUID.fromString(elementData.secondaryCollaboratorId);
                    Collaborator collaborator = collaboratorRepository.findById(id);
                    element.setSecondary(collaborator);
                }
                elements.add(element);
            }
            consecration.setElements(elements);

            consecrations.add(consecration);
        }

        return consecrations;
    }

    @Transactional
    @Override
    public void save(Consecration consecration) {
        jdbcTemplate.update("insert into Consecration (id, name, communication_message, event_id) values (?, ?, ?)",
            consecration.getId().toString(), consecration.getName(), consecration.getMessage().getContent(), consecration.getEvent().getId().toString());
        for (Element element : consecration.getElements()) {
            jdbcTemplate.update("insert into Element (id, name, quantity, unit, consecration_id, primary_collaborator_id, secondary_collaborator_id) values (?, ?, ?, ?, ?, ?)",
                element.getId().toString(),
                element.getName(),
                element.getQuantity(),
                element.getUnit(),
                consecration.getId().toString(),
                element.getPrimary().getPerson().getId().toString(),
                element.getSecondary().getPerson().getId().toString());
        }
    }

    @Transactional
    @Override
    public void update(Consecration consecration) {
        jdbcTemplate.update("delete from Element where consecration_id=?", consecration.getId().toString());
        jdbcTemplate.update("delete from Consecration where id=?", consecration.getId().toString());
        save(consecration);
    }
}
