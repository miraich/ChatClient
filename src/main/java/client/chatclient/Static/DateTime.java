package client.chatclient.Static;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTime {
    public static ZonedDateTime currentTime;
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
}
