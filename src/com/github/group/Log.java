package com.github.group;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Log {

    private static Log instance = null;

    public static final String ERROR   = "\u001B[31mERROR\u001B[0m";
    public static final String INFO    = "\u001B[33mINFO\u001B[0m";
    public static final String MESSAGE = "\u001B[32mMESSAGE\u001B[0m";

    /**
     * Constructor
     */
    protected Log() {
    }

    /**
     * Returns a singleton instance of Log
     *
     * @return An instance of Log
     */
    public static Log getInstance() {

        if (instance == null) {
            instance = new Log();
        }

        return instance;

    }

    /**
     * Prints out a log message in the format of 
     * HH:mm:ss type message
     *
     * @param   t   Message type 
     * @param   c   Calling method
     * @param   m   Input message
     */
    public void printLogMessage(String t, String c, String m) {

        // Set formatting options and get timestamp
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String timestamp = sdf.format(cal.getTime());

        // Print out log message
        System.out.println("[" + timestamp + "] [" + t + "] [" + c + "] " + m);
    }

}
