package model;

// class that prints events in EventLog
public class LogPrinter {
    public LogPrinter() {
        // pass
    }

    // EFFECTS: prints all events logged EventLog to console
    public static void printLog(EventLog eventLog) {
        for (Event event : eventLog) {
            System.out.println(event.toString());
        }
    }
}
