package org.religion.umbanda.tad.service.impl;

import org.joda.time.DateTime;
import org.religion.umbanda.tad.model.Event;
import org.religion.umbanda.tad.model.VisibilityType;
import org.religion.umbanda.tad.service.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Repository
public class EventRepositoryImpl implements EventRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Event> eventRowMapper = new RowMapper<Event>() {
        @Override
        public Event mapRow(ResultSet resultSet, int i) throws SQLException {
            final Event event = new Event();
            event.setId(UUID.fromString(resultSet.getString("id")));
            event.setTitle(resultSet.getString("title"));
            event.setNotes(resultSet.getString("notes"));
            event.setDate(new DateTime(resultSet.getLong("event_date")));
            event.setVisibility(VisibilityType.fromValue(resultSet.getInt("visibility_type")));
            event.setBackColor(resultSet.getString("back_color"));
            event.setFontColor(resultSet.getString("font_color"));
            return event;
        }
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
    public List<Event> findEventByYear(int year) {
        return jdbcTemplate.query("select * from Event where event_year = ?", new Object[] { year }, eventRowMapper);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Event> findEventByYear(int year, VisibilityType visibilityType) {
        return jdbcTemplate.query("select * from Event where event_year = ? and visibility_type = ?", new Object[] { year, visibilityType.getValue() }, eventRowMapper);
    }

    @Transactional
    @Override
    public void addEvent(Event event) {
        jdbcTemplate.update("insert into Event (id, title, notes, event_date, event_year, visibility_type, back_color, font_color) values (?, ?, ?, ?, ?, ?, ?, ?)",
            event.getId().toString(), event.getTitle(), event.getNotes(), event.getDate().getMillis(), event.getYear(), event.getVisibility().getValue(), event.getBackColor(), event.getFontColor());
    }

    @Transactional
    @Override
    public void updateEvent(Event event) {
        jdbcTemplate.update("update Event set title=?, notes=?, event_date=?, event_year=?, visibility_type=?, back_color=?, font_color=? where id=?",
            event.getTitle(), event.getNotes(), event.getDate().getMillis(), event.getYear(), event.getVisibility().getValue(), event.getId().toString(), event.getBackColor(), event.getFontColor());
    }

    @Transactional
    @Override
    public void removeEventById(UUID id) {
        jdbcTemplate.update("delete from Event where id = ?", id.toString());
    }
}
