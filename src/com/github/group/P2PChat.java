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
    private static NodeServer ns = null;

    public static String    username;
    public static String    id;

    /**
     * MAIN
     */
    public static void main (String args[]) {

        // Get instances of Crypto and Log
        log = Log.getInstance();
        c = Crypto.getInstance();

        // Get Port, Username and ID
        username = "cjhutchi-" + c.getID();
        id = c.getID();

        // Start services
        log.printLogMessage(Log.INFO, CLASS_ID, "Starting NodeServer");
        ns = NodeServer.getInstance();
        log.printLogMessage(Log.INFO, CLASS_ID, "Starting BroadcastServer");
        (new BroadcastServer()).start();
        log.printLogMessage(Log.INFO, CLASS_ID, "Starting BroadcastClient");
        (new BroadcastClient()).start();
        
        

    }

}
