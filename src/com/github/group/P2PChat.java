/**
 *  P2PChat.java
 *
 *  @author Cory Hutchison
 *  @author Frankie Yuan
 *  @author Jeremy Mah
 */
package com.github.group;

import java.util.Random;

public class P2PChat {

    private final static String CLASS_ID = "P2PChat";
    private static Log log;
    private static Crypto c;

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
        Random r = new Random();
        username = String.valueOf(r.nextInt(10));//"cjhutchi-" + c.getID();
        id = c.getID();

        // Start services
        log.printLogMessage(Log.INFO, CLASS_ID, "Starting NodeServer");
        NodeServer ns = NodeServer.getInstance();	// init
        PeerList.getInstance();	    // init
        GroupList.getInstance();    // init
        log.printLogMessage(Log.INFO, CLASS_ID, "Starting BroadcastServer");
        (new BroadcastServer()).start();
        log.printLogMessage(Log.INFO, CLASS_ID, "Starting BroadcastClient");
        (new BroadcastClient()).start();

        (new Shell(ns)).start();
    }
}
