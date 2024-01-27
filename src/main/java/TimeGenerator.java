import java.time.LocalDateTime;

public class TimeGenerator {
    public static String getCurrentTime() {
        LocalDateTime lDT = LocalDateTime.now();
        int hourInt = lDT.getHour();
        String hourString;
        if (hourInt > 9) {
            hourString = Integer.toString(hourInt);
        } else {
            hourString = "0" + hourInt;
        }

        int minuteInt = lDT.getMinute();
        String minuteString;
        if (minuteInt > 9) {
            minuteString = Integer.toString(minuteInt);
        } else {
            minuteString = "0" + minuteInt;
        }

        int secondInt = lDT.getSecond();
        String secondString;
        if (secondInt > 9) {
            secondString = Integer.toString(secondInt);
        } else {
            secondString = "0" + secondInt;
        }
        return String.format("[%s:%s:%s]", hourString, minuteString, secondString);
    }
}
