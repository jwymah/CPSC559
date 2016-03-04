/**
 *  MessageClient.java
 *
 *  @author Cory Hutchison
 *  @author Frankie Yuan
 *  @author Jeremy Mah
 */
package com.github.group;

public class MessageClient extends Thread {

    private final String CLASS_ID = "Message";
    private static Log log;

    public MessageClient() {

        // Get instance of Log
        log = Log.getInstance();

    }

}
