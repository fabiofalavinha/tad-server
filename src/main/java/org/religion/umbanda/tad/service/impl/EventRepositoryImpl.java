package org.religion.umbanda.tad.service.impl;

import org.joda.time.DateTime;
import org.religion.umbanda.tad.model.Event;
import org.religion.umbanda.tad.model.EventCategory;
import org.religion.umbanda.tad.model.VisibilityType;
import org.religion.umbanda.tad.service.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class EventRepositoryImpl implements EventRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Event> eventRowMapper = (resultSet, i) -> {
        final Event event = new Event();
        event.setId(UUID.fromString(resultSet.getString("id")));
        event.setTitle(resultSet.getString("title"));
        event.setNotes(resultSet.getString("notes"));
        event.setDate(new DateTime(resultSet.getLong("event_date")));
        event.setVisibility(VisibilityType.fromValue(resultSet.getInt("visibility_type")));
        event.setBackColor(resultSet.getString("back_color"));
        event.setFontColor(resultSet.getString("font_color"));
        event.setEventCategory(EventCategory.fromValue(resultSet.getInt("category")));
        return event;
    };

    @Autowired
    public EventRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(UUID id) {
        return jdbcTemplate.queryForObject("select count(*) as Count from Event where id = ?", Integer.class, id.toString()) == 1;
    }

    @Transactional(readOnly = true)
    @Override
    public Event findById(UUID id) {
        try {
            return jdbcTemplate.queryForObject("select * from Event where id=?", new Object[] { id.toString() }, eventRowMapper);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Event> findByTitleAndDate(String title, DateTime date) {
        try {
            return Optional.of(jdbcTemplate.queryForObject("select * from Event where title = ? and event_date = ?", eventRowMapper, title, date.getMillis()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<Event> findEventByYear(int year) {
        return jdbcTemplate.query("select * from Event where event_year = ?", new Object[]{year}, eventRowMapper);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Event> findEventByYear(int year, VisibilityType visibilityType) {
        return jdbcTemplate.query("select * from Event where event_year = ? and visibility_type = ?", new Object[]{year, visibilityType.getValue()}, eventRowMapper);
    }

    @Transactional
    @Override
    public void addEvent(Event event) {
        jdbcTemplate.update("insert into Event (id, title, notes, event_date, event_year, visibility_type, back_color, font_color, category) values (?, ?, ?, ?, ?, ?, ?, ?, ?)",
            event.getId().toString(), event.getTitle(), event.getNotes(), event.getDate().getMillis(), event.getYear(), event.getVisibility().getValue(), event.getBackColor(), event.getFontColor(), event.getEventCategory().getValue());
    }

    @Transactional
    @Override
    public void updateEvent(Event event) {
        jdbcTemplate.update("update Event set title=?, notes=?, event_date=?, event_year=?, visibility_type=?, back_color=?, font_color=?, category=? where id=?",
            event.getTitle(), event.getNotes(), event.getDate().getMillis(), event.getYear(), event.getVisibility().getValue(), event.getBackColor(), event.getFontColor(), event.getEventCategory().getValue(), event.getId().toString());
    }

    @Transactional
    @Override
    public void removeEventById(UUID id) {
        jdbcTemplate.update("delete from Event where id = ?", id.toString());
    }
}
