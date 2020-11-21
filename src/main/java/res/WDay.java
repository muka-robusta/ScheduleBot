package res;

import java.util.Calendar;
import java.util.GregorianCalendar;

public enum WDay {
    MON, TUE, WED, THR, FRI, SAT, SUN;
    public static String getUkrName(WDay day) {
        return switch(day) {
            case MON:
                yield "Понеділок";
            case TUE:
                yield "Вівторок";
            case WED:
                yield "Середа";
            case THR:
                yield "Четвер";
            case FRI:
                yield "П'ятниця";
            case SAT:
                yield "Субота";
            case SUN:
                yield "Неділя";
        };
    }

    public static WDay convertFromCalendar(GregorianCalendar calendar) {
        final int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        return switch (weekDay) {
            case 1:
                yield WDay.SUN;
            case 2:
                yield WDay.MON;
            case 3:
                yield WDay.TUE;
            case 4:
                yield WDay.WED;
            case 5:
                yield WDay.THR;
            case 6:
                yield WDay.FRI;
            case 7:
                yield WDay.SAT;
            default:
                throw new IllegalStateException("Unexpected value: " + weekDay);
        };
    }
}
