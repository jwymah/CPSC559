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
import java.util.Calendar;
import java.util.Random;
import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import sun.misc.BASE64Encoder;


public class P2PChat {

    final static int MIN_PORT = 9000;
    final static int MAX_PORT = 9025;

    private static PublicKey    pub;
    private static PrivateKey   priv;

    public static String    username;
    public static String    id;
    public static int       port;
    public static boolean   isRunning;

    public static void main (String args[]) {

        isRunning = true;
        System.out.println("- P2P Group chat");

        port = generatePort();
        username = "testing";

        try {
            id = generateID();
        } catch (NoSuchAlgorithmException e) {
        } catch (NoSuchProviderException e) {
        }

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

    /**
     * Generates a Public and Private key and returns the public key as the user
     * ID.
     *
     * @return A public key in Hex encoding
     */
    private static String generateID() throws 
        NoSuchAlgorithmException, NoSuchProviderException {

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        keyGen.initialize(512, random);
        KeyPair pair = keyGen.generateKeyPair();

        priv = pair.getPrivate();
        pub = pair.getPublic();
        
        /**
         * TODO:    Find a way to display this nicely it is showing up as a
         *          huge string right now, might have to change Algorithm.
         */
        BASE64Encoder encoder = new BASE64Encoder();
        String s = encoder.encode(pub.getEncoded());

        return s;

    }
}
