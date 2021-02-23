package utilities;


import javafx.scene.control.ComboBox;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * TimeConversion class. Utility class that provides methods to handle the formatting of dates and times, conversions, and comparisons.
 */
public class TimeConversion {
    /**
     * formatDate. Formats a localDateTime to a readable format of named month day, and year.
     * @param localDateTime LocalDateTime
     * @return String formatted localDateTime
     */
    public static String formatDate(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        return formatter.format(localDateTime);

    }

    /**
     * formatTime. Formats a localDateTime to a readable 12 hour time with am/pm.
     * @param localDateTime LocalDateTime
     * @return String localDateTime
     */
    public static String formatTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
        return formatter.format(localDateTime);
    }

    /**
     * formatTime. Overload formatTime method that accepts a LocalTime object and formats to a readable 12 hour time with am/pm
     * @param localTime LocalTime
     * @return String formatted localTime
     */
    public static String formatTime(LocalTime localTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        return formatter.format(localTime);
    }

    /**
     * formatTime. Overload formatTime method that accepts a ZonedDateTime object and formats to a readable 12 hour time with am/pm
     * @param zonedDateTime zonedDateTime
     * @return String formatted zonedDateTime
     */
    public static String formatTime(ZonedDateTime zonedDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        return formatter.format(zonedDateTime);
    }

    /**
     * populateTimes. Takes a combo box, a start time and end time, and populates the combo boxes with times.
     * @param localTimeComboBox comboBox
     * @param startTime LocalTime
     * @param endTime LocalTime
     * @return ComboBox<LocalTime>
     */
    public static ComboBox<LocalTime> populateTimes(ComboBox<LocalTime> localTimeComboBox, LocalTime startTime, LocalTime endTime) {
        while(startTime.isBefore(endTime.plusSeconds(1))) {
            localTimeComboBox.getItems().add(startTime);
            startTime = startTime.plusMinutes(15);
        }
        return localTimeComboBox;
    }

    /**
     * convertLocalToEastern. Coverts local time to eastern time for use with comparison methods.
     * @param timeToConvert LocalDateTime
     * @return LocalDateTime converted to eastern time
     */
    public static LocalDateTime convertLocalToEastern(LocalDateTime timeToConvert) {
        // Eastern Time Zone
        ZoneId easternTime = ZoneId.of("America/New_York");
        ZoneId localZone = ZoneId.systemDefault();
        LocalDateTime localTime = timeToConvert;
        // convert to eastern time
        ZonedDateTime currentLocalTime = localTime.atZone(localZone);
        ZonedDateTime currentEasternTime = currentLocalTime.withZoneSameInstant(easternTime);

        return currentEasternTime.toLocalDateTime();
    }

    /**
     * compareWithBusinessHours. Compares local time to eastern business hours of 8AM to 10PM. Returns true if hours are
     * between business hours, otherwise returns false.
     * @param timeToCompare LocalDateTime
     * @return boolean, true or false depending on where the local hour falls between.
     */
    public static boolean compareWithBusinessHours(LocalDateTime timeToCompare) {
        // set an eastern time starting hour and eastern time ending hour

        LocalTime initialStartTime = LocalTime.of(7,59);
        ZoneId easternZoneId = ZoneId.of("America/New_York");
        ZonedDateTime easternStart = ZonedDateTime.of(timeToCompare.toLocalDate(), initialStartTime, easternZoneId);
        System.out.println("eastern zoned start time " + easternStart + " " + easternStart.toLocalDateTime());

        LocalTime initialEndTime = LocalTime.of(22, 0);
        ZonedDateTime easternEnd = ZonedDateTime.of(timeToCompare.toLocalDate(), initialEndTime, easternZoneId);
        System.out.println("eastern zoned end time " + easternEnd + " " + easternEnd.toLocalDateTime());

//        LocalDateTime startHour = convertLocalToEastern(LocalDateTime.of(timeToCompare.toLocalDate(), LocalTime.of(7,59)));
//        LocalDateTime endHour = convertLocalToEastern(LocalDateTime.of(timeToCompare.toLocalDate(), LocalTime.of(22,0)));
        // find current time to compare with
        LocalDateTime easternTimeToCompare = convertLocalToEastern(timeToCompare);

//        System.out.println("Converting time to compare to eastern time");
//        System.out.println("local time: " + TimeConversion.formatTime(timeToCompare));
//        System.out.println("converted eastern time: " + TimeConversion.formatTime(easternTimeToCompare));
//        System.out.println("comparing time is after start hour: " + TimeConversion.formatTime(easternStart) + " is " + easternTimeToCompare.isAfter(ChronoLocalDateTime.from(easternStart)));
//        System.out.println("comparing time is before end hour: " + TimeConversion.formatTime(easternEnd) + " is " + easternTimeToCompare.isBefore(ChronoLocalDateTime.from(easternEnd)));

        // return boolean true if in between eastern business hours, otherwise false
        return easternTimeToCompare.isAfter(ChronoLocalDateTime.from(easternStart)) && easternTimeToCompare.isBefore(ChronoLocalDateTime.from(easternEnd));
    }
}
