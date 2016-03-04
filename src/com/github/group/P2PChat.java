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
import java.util.Calendar;
import java.util.Random;
import java.text.SimpleDateFormat;


public class P2PChat {

    final static int MIN_PORT = 9000;
    final static int MAX_PORT = 9025;

    //private static PublicKey    pub;
    //private static PrivateKey   priv;

    public static String    username;
    public static String    id;
    public static int       port;
    public static boolean   isRunning;

    public static void main (String args[]) {

        Crypto c = Crypto.getInstance();

        isRunning = true;
        System.out.println("- P2P Group chat");

        port = generatePort();
        username = "cjhutchi";

        id = c.getID();

        System.out.println("[*] Staring Broadcast Server..");
        (new BroadcastServer()).start();
        System.out.println("[*] Starting Broadcast Client..");
        (new BroadcastClient()).start();
        System.out.println("[*] Starting Message Server..");
        (new MessageServer(port)).start();

    }

    /**
     * Returns a timestamp string in HH:mm:ss format.
     *
     * @return  HH:mm:ss
     */
    public static String getTimeStamp() {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        return sdf.format(cal.getTime());

    }

    /**
     * Generates a random integer between MIN_PORT and MAX_PORT.
     *
     * @return A random integer between MIN_PORT and MAX_PORT
     */
    private static int generatePort() {

        Random rand = new Random(); 

        return rand.nextInt((MAX_PORT - MIN_PORT) + 1) + MIN_PORT;

    }
}
