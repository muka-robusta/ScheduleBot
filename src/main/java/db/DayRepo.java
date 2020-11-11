package db;

import model.Day;
import res.WDay;

import java.util.List;

public interface DayRepo {
    List<Day> getSchedule();
    Day getScheduleByDate(WDay day);
    void create(Day day);
    void update(Day day);
    void delete(Day day);

}
