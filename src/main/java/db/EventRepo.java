package db;

import model.Event;

import java.util.List;

public interface EventRepo {
    List<Event> getEvents();
    Event getEvent(int id);
    void create(Event item);
    void update(Event item);
    void delete(Event item);
}
