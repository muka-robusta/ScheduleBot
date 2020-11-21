package db;

import model.Event;
import res.WDay;

import java.sql.SQLException;
import java.util.List;

public interface EventRepo {
    List<Event> getEvents() throws SQLException;
    Event getEvent(int id) throws SQLException;
    void create(Event item) throws SQLException;
    void update(Event item) throws SQLException;
    void delete(int id) throws SQLException;
    List<Event> getEventsByDay(WDay day) throws SQLException;
}
