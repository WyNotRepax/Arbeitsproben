package de.hsos.roomplanner.util.date;

import java.time.LocalDate;

/**
 * Helper Class for limiting {@link java.time.LocalDate}.
 * @author Benno Steinkamp
 */
public class DateUtil {

    public static final int MIN_DATE_DAY = 1;
    public static final int MIN_DATE_MONTH = 1;
    public static final int MIN_DATE_YEAR = 1900;
    public static final LocalDate MIN_DATE = LocalDate.of(MIN_DATE_YEAR, MIN_DATE_MONTH, MIN_DATE_DAY);

    public static final int MAX_DATE_DAY = 31;
    public static final int MAX_DATE_MONTH = 12;
    public static final int MAX_DATE_YEAR = 9999;
    public static final LocalDate MAX_DATE = LocalDate.of(MAX_DATE_YEAR, MAX_DATE_MONTH, MAX_DATE_DAY);

    public static boolean isValid(LocalDate date) {
        return !date.isBefore(MIN_DATE) && !date.isAfter(MAX_DATE);
    }

    public static LocalDate maxIfNull(LocalDate date) {
        if (date == null) {
            return MAX_DATE;
        }
        return date;
    }

    public static LocalDate minIfNull(LocalDate date) {
        if (date == null) {
            return MIN_DATE;
        }
        return date;
    }

    public static boolean isBetween(LocalDate date, final LocalDate fromDate, final LocalDate toDate) {
        return !fromDate.isBefore(date) && !toDate.isAfter(date);
    }

}
