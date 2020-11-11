package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import res.WDay;

import java.util.List;

@Getter
@Setter
@ToString
public class Day {
    private WDay dayOfWeek;
    private List<Event> events;
}
