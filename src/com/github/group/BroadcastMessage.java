/**
 *  BroadcastMessage.java
 *
 *  @author Cory Hutchison
 *  @author Frankie Yuan
 *  @author Jeremy Mah
 */
package com.github.group;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONObject;

public class BroadcastMessage extends Message {
    private String id;
    private String ip;
    private String pub;
    private int port;

    private JSONObject msg;

    /**
     * Constructor
     */
    public BroadcastMessage() {
        super();

        // TODO:    Get ID, IP, and Port # from Server
        // Placeholder values
        id = P2PChat.username;
        pub = P2PChat.id;
        ip = "127.0.0.1";
        port = P2PChat.port;

        msg = new JSONObject();
        msg.put("TimeStamp", super.timestamp);
        msg.put("ID", id);
        msg.put("IP", ip);
        msg.put("PublicKey", pub);
        msg.put("Port", port);
    }

    public String toString() {
        return msg.toString();
    }
}

