/**
 *  P2PChat.java
 *
 *  @author Cory Hutchison
 *  @author Frankie Yuan
 *  @author Jeremy Mah
 */
package com.github.group;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.CloneNotSupportedException;
import java.util.Random;


public class P2PChat {

    private final static String CLASS_ID = "P2PChat";
    private static Log log;
    private static Crypto c;

    final static int MIN_PORT = 9000;
    final static int MAX_PORT = 9025;

    public static String    username;
    public static String    id;
    public static int       port;

    /**
     * MAIN
     */
    public static void main (String args[]) {

        // Get instances of Crypto and Log
        log = Log.getInstance();
        c = Crypto.getInstance();

        // Get Port, Username and ID
        port = getPort();
        username = "cjhutchi";
        id = c.getID();

        // Start services
        log.printLogMessage(Log.INFO, CLASS_ID, "Starting BroadcastServer");
        (new BroadcastServer()).start();
        log.printLogMessage(Log.INFO, CLASS_ID, "Starting BroadcastClient");
        (new BroadcastClient()).start();
        log.printLogMessage(Log.INFO, CLASS_ID, "Starting MessageServer");
        (new MessageServer(port)).start();

        log.printLogMessage(Log.INFO, CLASS_ID, "Startup complete");

    }

    /**
     * Generates a random integer between MIN_PORT and MAX_PORT.
     *
     * @return A random integer between MIN_PORT and MAX_PORT
     */
    private static int getPort() {
       
        log.printLogMessage(Log.INFO, CLASS_ID, "Generating Port");

        Random rand = new Random(); 

        return rand.nextInt((MAX_PORT - MIN_PORT) + 1) + MIN_PORT;

    }
}
