package db.impl;

import db.EventRepo;
import db.SubjectRepo;
import h2.H2Context;
import model.Event;
import model.Subject;
import res.WDay;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class EventRepoImpl implements EventRepo {

    private H2Context h2base;

    public EventRepoImpl() {
        try {

            h2base = H2Context.getInstance();
            Statement statement = h2base.getConnection().createStatement();

            String createTableSqlStatement = "CREATE TABLE IF NOT EXISTS events " +
                    "( event_id INTEGER not NULL, " +
                    "subject_id INTEGER, " +
                    "event_time VARCHAR(6)," +
                    "event_week_day VARCHAR(4)," +
                    "queue_flag INTEGER," +
                    "week_spec INTEGER," +
                    "event_type VARCHAR(40),"+
                    "PRIMARY KEY (event_id)," +
                    "FOREIGN KEY (subject_id) REFERENCES subjects(subject_id)" +
                    ");";

            statement.executeUpdate(createTableSqlStatement);
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Event> getEvents() throws SQLException {
        String sqlStatement = "SELECT * FROM events;";
        final Statement statement = h2base.getConnection()
                .createStatement();
        final ResultSet resultSet = statement.executeQuery(sqlStatement);

        List<Event> events = new ArrayList<>();

        while(resultSet.next()) {

            String[] timeSequence = resultSet.getString("event_time").split(":");

            final SubjectRepoImpl subjectRepo = new SubjectRepoImpl();
            final Subject linkedSubject = subjectRepo.getSubject(resultSet.getInt("subject_id"));

            final Event event = Event.generateByValues(
                    resultSet.getInt("event_id"),
                    linkedSubject,
                    resultSet.getString("event_type"),
                    Integer.parseInt(timeSequence[0]),
                    Integer.parseInt(timeSequence[1]),
                    WDay.valueOf(resultSet.getString("event_week_day")),
                    resultSet.getInt("queue_flag"),
                    resultSet.getInt("week_spec")
            );
            events.add(event);
        }
        resultSet.close();
        statement.close();

        return events;
    }

    @Override
    public Event getEvent(int id) throws SQLException {
        String sqlStatement = "SELECT * FROM events WHERE event_id = " + id + ";";
        final Statement statement = h2base.getConnection().createStatement();
        final ResultSet resultSet = statement.executeQuery(sqlStatement);

        if(resultSet.first())
        {
            final String[] event_times = resultSet.getString("event_time").split(":");

            final SubjectRepoImpl subjectRepo = new SubjectRepoImpl();
            final Subject linkedSubject = subjectRepo.getSubject(resultSet.getInt("subject_id"));

            final Event event = Event.generateByValues(resultSet.getInt("event_id"),
                    linkedSubject,
                    resultSet.getString("event_type"),
                    Integer.parseInt(event_times[0]),
                    Integer.parseInt(event_times[1]),
                    WDay.valueOf(resultSet.getString("event_week_day")),
                    resultSet.getInt("queue_flag"),
                    resultSet.getInt("week_spec")
            );
            resultSet.close();
            statement.close();

            return event;
        } else {
            resultSet.close();
            statement.close();

            throw new RuntimeException("Event not found");
        }

    }

    @Override
    public void create(Event item) throws SQLException {

        final GregorianCalendar time = item.getTime();
        String eventTime = time.get(Calendar.HOUR) + ":" + time.get(Calendar.MINUTE);

        String prepareSqlStatementString = "INSERT INTO events VALUES(?,?,?,?,?,?,?);";
        final PreparedStatement preparedStatement = h2base.getConnection().prepareStatement(prepareSqlStatementString);
        preparedStatement.setInt(1, item.getEventId());
        preparedStatement.setInt(2, item.getSubject().getId());
        preparedStatement.setString(3, eventTime);
        preparedStatement.setString(4, item.getWeekDay().toString());
        preparedStatement.setInt(5, item.getQueueFlag());
        preparedStatement.setInt(6, item.getWeekSpec());
        preparedStatement.setString(7, item.getEventType());

        if(preparedStatement.executeUpdate() == 0)
            throw new RuntimeException("Unable to execute insert");

        preparedStatement.close();
    }

    public void update(Event event) throws SQLException {
        final GregorianCalendar eventTime = event.getTime();
        String eventTimeString = eventTime.get(Calendar.HOUR) + ":" + eventTime.get(Calendar.MINUTE);
        String sqlStatement = "UPDATE events " +
                "SET subject_id = ?," +
                "event_time = ?, " +
                "queue_flag = ?, " +
                "event_week_day = ?, " +
                "week_spec = ?, " +
                "event_type = ?, " +
                "WHERE event_id = ?;";
        final PreparedStatement preparedStatement = h2base.getConnection().prepareStatement(sqlStatement);
        preparedStatement.setInt(1, event.getSubject().getId());
        preparedStatement.setString(2, eventTimeString);
        preparedStatement.setInt(3, event.getQueueFlag());
        preparedStatement.setString(4, event.getWeekDay().toString());
        preparedStatement.setInt(5, event.getWeekSpec());
        preparedStatement.setString(6, event.getEventType());
        preparedStatement.setInt(7, event.getEventId());

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }


    @Override
    public void delete(int id) throws SQLException {
        String sqlStatement = "DELETE FROM events WHERE event_id = ?;";
        final PreparedStatement preparedStatement = h2base.getConnection().prepareStatement(sqlStatement);
        preparedStatement.setInt(1,id);
        preparedStatement.executeUpdate(sqlStatement);
        preparedStatement.close();
    }

    @Override
    public List<Event> getEventsByDay(WDay weekDay) throws SQLException {
        String sqlStatement = "SELECT * FROM events WHERE event_week_day = ? ORDER BY queue_flag;";
        final PreparedStatement preparedStatement = h2base.getConnection().prepareStatement(sqlStatement);
        preparedStatement.setString(1, weekDay.toString());
        final SubjectRepoImpl subjectRepo = new SubjectRepoImpl();

        final ResultSet resultSet = preparedStatement.executeQuery();


        List<Event> filteredEvents = new ArrayList<>();
        while (resultSet.next()) {

            final String[] eventTimeStringList = resultSet.getString("event_time").split(":");

            final Event event = Event.generateByValues(
                    resultSet.getInt("event_id"),
                    subjectRepo.getSubject(resultSet.getInt("subject_id")),
                    resultSet.getString("event_type"),
                    Integer.parseInt(eventTimeStringList[0]),
                    Integer.parseInt(eventTimeStringList[1]),
                    WDay.valueOf(resultSet.getString("event_week_day")),
                    resultSet.getInt("queue_flag"),
                    resultSet.getInt("week_spec")

            );
            filteredEvents.add(event);
        }
        resultSet.close();
        preparedStatement.close();

        return filteredEvents;

    }
}
