/**
 *  BroadcastMessage.java
 *
 *  @author Cory Hutchison
 *  @author Frankie Yuan
 *  @author Jeremy Mah
 */
package com.github.group;

import java.net.NetworkInterface;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONObject;

public class BroadcastMessage extends Message {

    private static String CLASS_ID = "BroadcastMessage";
    private static Log log;

    public String username;
    public String id;
    public String ip;
    public int port;

    private JSONObject msg;
    /**
     * Constructor
     */
    public BroadcastMessage() {
        super(MessageType.BROADCAST);

        // Get instance of Log
        log = Log.getInstance();

        // Set components
        username = P2PChat.username;
        id = P2PChat.id;
        ip = NodeServer.getIP();
        port = NodeServer.getPort();

        // Package in JSON object
        msg = new JSONObject();
        msg.put("timestamp", super.timestamp);
        msg.put("type", super.type.toString());
        msg.put("username", username);
        msg.put("id", id);
        msg.put("ip", ip);
        msg.put("port", port);
    }

    /**
     * Constructor that parses an input message
     * @param m the message
     */
    public BroadcastMessage(String m) {
    	super(MessageType.BROADCAST);

        // Remove weird added whitespace that rekt parsing
        // and initialize JSON parser
        m = m.trim();   
        JSONParser parser = new JSONParser();

        // Parse message and get message components
        try {
            Object obj = parser.parse(m);

            msg = (JSONObject) obj;

            timestamp = (long) msg.get("timestamp");
            username = (String) msg.get("username");
            id = (String) msg.get("id");
            ip = (String) msg.get("ip");
            port = ((Long) msg.get("port")).intValue();
        } catch (ParseException e) {
            log.printLogMessage(Log.ERROR, CLASS_ID, "Received invalid BroadcastMessage");
            //System.out.println(m);
        }
    }

    /**
     * Returns a BroadcastMessage as JSON in a string format
     *
     * @return JSON BroadcastMessage as string
     */
    @Override
    public String toJsonString() {
        return msg.toString();
    }

    /**
     * Writes out the key/vals of a BroadcastMessage in a legible format
     */
    public void printMessage() {
        log.printLogMessage(Log.INFO, CLASS_ID, "");

        System.out.println();
        System.out.println("\tTimestamp:\t" + timestamp);
        System.out.println("\tUsername:\t" + username);
        System.out.println("\tID:\t\t" + id);
        System.out.println("\tIP:\t\t" + ip);
        System.out.println("\tPort:\t\t" + port);
        System.out.println();        
    }

	@Override
	public void setDst(String username)
	{
        // Do nothing
	}

	@Override
	public void signMessage()
	{
		// Do nothing
	}
}

