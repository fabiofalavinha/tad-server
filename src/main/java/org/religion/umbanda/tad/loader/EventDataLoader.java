package org.religion.umbanda.tad.loader;

import org.joda.time.DateTime;
import org.religion.umbanda.tad.log.Log;
import org.religion.umbanda.tad.log.LogFactory;
import org.religion.umbanda.tad.model.Event;
import org.religion.umbanda.tad.model.VisibilityType;
import org.religion.umbanda.tad.service.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Event file structure:
 *
 * Line => TITLE;DATE;NOTES;VISIBILITY
 * . DATE => dd/MMM/yy (brazilian format)
 * . VISIBILITY => PUBLICO or INTERNO
 */
@Component
public class EventDataLoader implements DataLoader {

    private static final Log log = LogFactory.createLog(EventDataLoader.class);
    private static final String KEY = "event.file";
    private static final String LINE_TOKEN_SEPARATOR = ";";
    private static final int HEADER_LINE = 1;

    @Autowired
    private EventRepository eventRepository;

    @Override
    public boolean accept(String key) {
        return KEY.equals(key);
    }

    @Override
    public void load(String data) {
        try (Stream<String> stream = Files.lines(Paths.get(data), Charset.forName("utf-8"))) {
            stream.forEach(l -> {
                try {
                    String[] lineTokens = l.split(LINE_TOKEN_SEPARATOR);

                    EventToken eventToken = new EventToken();
                    eventToken.setTitle(lineTokens[0].trim());
                    eventToken.setDate(lineTokens[1].trim());
                    eventToken.setNotes(lineTokens[2].trim());
                    eventToken.setVisibility(lineTokens[3].trim());

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yy", new Locale("pt", "BR"));
                    Date date = sdf.parse(eventToken.getDate());
                    DateTime eventDate = new DateTime(date);

                    String visibility = eventToken.getVisibility();
                    VisibilityType visibilityType;
                    if ("PUBLICO".equals(visibility)) {
                        visibilityType = VisibilityType.PUBLIC;
                    } else if ("INTERNO".equals(visibility)) {
                        visibilityType = VisibilityType.INTERNAL;
                    } else {
                        throw new IllegalArgumentException(String.format("Error parsing visibility [%s]", visibility));
                    }

                    Optional<Event> eventOptional = eventRepository.findByTitleAndDate(eventToken.getTitle(), eventDate);
                    if (eventOptional.isPresent()) {
                        Event existed = eventOptional.get();
                        existed.setNotes(eventToken.getNotes());
                        existed.setVisibility(visibilityType);
                        eventRepository.updateEvent(existed);
                    } else {
                        Event newEvent = new Event();
                        newEvent.setTitle(eventToken.getTitle());
                        newEvent.setDate(eventDate);
                        newEvent.setNotes(eventToken.getNotes());
                        newEvent.setVisibility(visibilityType);
                        eventRepository.addEvent(newEvent);
                    }

                } catch (Exception ex) {
                    log.exception(ex, "Error parsing events [%s]", l);
                }
            });
        } catch (IOException e) {
            log.exception(e, "Error reading file [%s]", data);
        }
    }

    private class EventToken {

        private String title;
        private String notes;
        private String date;
        private String visibility;
        private String backColor;
        private String fontColor;

        public String getTitle() { return title;}

        public void setTitle(String title) { this.title = title;}

        public String getNotes() { return notes;}

        public void setNotes(String notes) { this.notes = notes;}

        public String getDate() { return date;}

        public void setDate(String date) { this.date = date;}

        public String getVisibility() { return visibility;}

        public void setVisibility(String visibility) { this.visibility = visibility;}

        public String getBackColor() { return backColor;}

        public void setBackColor(String backColor) { this.backColor = backColor;}

        public String getFontColor() { return fontColor;}

        public void setFontColor(String fontColor) { this.fontColor = fontColor;}
    }
}
