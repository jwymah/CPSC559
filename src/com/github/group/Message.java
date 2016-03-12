/**
 *  Message.java
 *
 *  @author  Cory Hutchison
 *  @author  Jeremy Mah
 *  @author  Frankie Yuan
 */ 
package com.github.group;

public abstract class Message {

    private final String CLASS_ID = "Message";
    private static Log log;

    public long timestamp;

    /**
     * Constructor
     */
    public Message() {

        // Get instance of log
        log = Log.getInstance();

        // Give message a UNIX Timestamp 
        // NOTE: Attempted on both Unix and Windows with success
        timestamp = (long) (System.currentTimeMillis() / 1000L);

    }

}
