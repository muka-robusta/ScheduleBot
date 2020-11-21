package model;

import lombok.*;
import res.WDay;

import java.util.Calendar;
import java.util.GregorianCalendar;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Event {
    private int eventId;
    private Subject subject;
    private GregorianCalendar time;
    private int queueFlag;
    private int weekSpec;
    private String eventType;
    private WDay weekDay;

    public static Event generateByValues(int eventId, Subject subject, String type, int hour, int minutes, WDay weekDay, int queueFlag, int weekSpec) {
        Event event = new Event();
        event.setEventId(eventId);
        event.setSubject(subject);
        event.setQueueFlag(queueFlag);
        event.setEventType(type);

        final GregorianCalendar eventTime = new GregorianCalendar();
        eventTime.set(Calendar.HOUR, hour);
        eventTime.set(Calendar.MINUTE, minutes);

        event.setTime(eventTime);
        event.setWeekSpec(weekSpec);
        event.setWeekDay(weekDay);

        return event;
    }

    public static GregorianCalendar convertToGCalendar(int hour, int minutes) {
        final GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.set(Calendar.HOUR, hour);
        gregorianCalendar.set(Calendar.MINUTE, minutes);
        return gregorianCalendar;
    }

}
