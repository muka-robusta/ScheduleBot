package model;

import lombok.*;

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
}
