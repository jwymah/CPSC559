/**
 *  MessageClient.java
 *
 *  @author Cory Hutchison
 *  @author Frankie Yuan
 *  @author Jeremy Mah
 */
package com.github.group;

import java.io.IOException;
import java.net.Socket;

public class MessageClient extends Thread {

    private final String CLASS_ID = "Message";
    private static Log log;

    private String ip;
    private int port;

    /**
     * Construction
     */
    public MessageClient(String s, int p) {

        // Get instance of Log
        log = Log.getInstance();

        // Set IP, Port
        ip = s;
        port = p;

    }

    /**
     * Thread execution code
     */
    public void run() {

        Socket connectToPeer = null;

        try {
            connectToPeer = new Socket(ip, port);
        } catch (IOException e) {
            log.printLogMessage(Log.ERROR, CLASS_ID, "Unable to connect to peer");
        }

    }

}
