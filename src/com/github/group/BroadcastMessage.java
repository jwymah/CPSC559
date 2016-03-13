/**
 *  BroadcastMessage.java
 *
 *  @author Cory Hutchison
 *  @author Frankie Yuan
 *  @author Jeremy Mah
 */
package com.github.group;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONObject;

public class BroadcastMessage extends Message {

    private static NodeServer ns = null;

    private static String CLASS_ID = "BroadcastMessage";
    private static Log log;

    public String username;
    public String id;
    public String ip;
    public int port;

    private JSONObject msg;
    private NetworkInterface net;

    /**
     * Constructor
     */
    public BroadcastMessage() {
        super();

        // Get instance of Log
        log = Log.getInstance();
        ns = NodeServer.getInstance();

        // Set components
        username = P2PChat.username;
        id = P2PChat.id;
        ip = ns.getIP();
        port = ns.getPort();

        // Package in JSON object
        msg = new JSONObject();
        msg.put("TimeStamp", super.timestamp);
        msg.put("Username", username);
        msg.put("ID", id);
        msg.put("IP", ip);
        msg.put("Port", port);
    }

    /**
     * Constructor that parses and input message
     */
    public BroadcastMessage(String m) {

        // Remove weird added whitespace that rekt parsing
        // and initialize JSON parser
        m = m.trim();   
        JSONParser parser = new JSONParser();

        // Parse message and get message components
        try {
            Object obj = parser.parse(m);

            msg = (JSONObject) obj;

            timestamp = (long) msg.get("TimeStamp");
            username = (String) msg.get("Username");
            id = (String) msg.get("ID");
            ip = (String) msg.get("IP");
            port = ((Long) msg.get("Port")).intValue();
        } catch (ParseException e) {
            log.printLogMessage(Log.ERROR, CLASS_ID, "Received invalid BroadcastMessage");
            System.out.println(m);
        }

    }

    /**
     * Returns a BroadcastMessage as JSON in a string format
     *
     * @return JSON BroadcastMessage as string
     */
    public String toString() {

        return msg.toString();

    }

    /**
     * Writes out the key/vals of a BroadcastMessage in a legible format
     */
    public void printMessage() {

        log.printLogMessage(Log.INFO, CLASS_ID, "");

        System.out.println();
        System.out.println("\tTimeStamp:\t" + timestamp);
        System.out.println("\tUsername:\t" + username);
        System.out.println("\tID:\t\t" + id);
        System.out.println("\tIP:\t\t" + ip);
        System.out.println("\tPort:\t\t" + port);
        System.out.println();
        
    }

}

