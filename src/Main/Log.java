package Main;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {

    private static boolean disabled = false;

    public static void logMessage(String message) {
        if (!disabled) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SSS    ");

            System.out.println(now.format(format) + message);
        }
        //https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
    }

    public static void enableLogging() {
        disabled = false;
    }

    public static void disableLogging() {
        disabled = true;
    }
}
