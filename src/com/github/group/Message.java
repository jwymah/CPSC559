/**
 * File:    Message.java
 *
 * This file represents the Message format that will be passed back and forth
 * between servers/clients.
 *
 * @author  Cory Hutchison
 * @author  Jeremy Mah
 * @author  Frankie Yuan
 * @version 0.0
 */ 
package com.github.group;

import java.lang.StringBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import org.json.simple.JSONObject;

/**
 *  {
 *      “TimeStamp”: “”, (timestamp, best effort)
 *      “HostID”: “”, (UUID, Sender)
 *      “GroupID”: “”, (UUID, Recipients)
 *      “MessageID”: “”, (hash)
 *      “MessageType”: “CONTROL”,
 *      “MessageBody”: “”, [“REQUEST TO JOIN”, “DISCOVERY”, ]   
 *  }
 */
public class Message {

    private JSONObject msg;

    private String timestamp;
    private String hostId;
    private String groupId;
    private String messageId;
    private String messageType;
    private String messageBody;

    /**
     * Constructor for a Message object.
     *
     * @param id    Local server id
     * @param gId   Group id (only set when broadcasting to group
     * @param type  Message type
     * @param m     Message body
     */
    public Message(String id, String gId, String type, String m) {
        // Time Stamp
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        timestamp = df.format(date);

        // Host ID
        hostId = id; 

        // Group ID
        groupId = gId;

        // Message ID
        messageId = generateMessageID(m);

        // Message Type
        messageType = type;

        // Message Body
        messageBody = m;

        // Package up into a JSON Object
        msg = new JSONObject();
        msg.put("TimeStamp", timestamp);
        msg.put("HostID", hostId);
        msg.put("GroupID", groupId);
        msg.put("MessageID", messageId);
        msg.put("MessageType", messageType);
        msg.put("MessageBody", messageBody);
    }

    /**
     * Returns the timestamp associated with this message object.
     *
     * @return  The timestamp of this message object
     */
    public String getTimeStamp() {
        return timestamp;
    }

    /**
     * Returns the host ID associated with this message.
     *
     * @return  The local host ID 
     */
    public String getHostID() {
        return hostId;
    }

    /**
     * Returns the group ID associated with this message.
     *
     * @return  The group ID
     */
    public String getGroupID() {
        return groupId;
    }

    /**
     * Returns the message ID associated with this message.
     *
     * @return  The message ID
     */
    public String getMessageID() {
        return messageId;
    }

    /**
     * Returns the message type associated with this message.
     *
     * @return  The message type
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * Returns the message body associated with this message.
     *
     * @return  The message body
     */
    public String getMessageBody() {
        return messageBody;
    }

    /**
     * Returns the JSON formatted message.
     *
     * @return  The message
     */
    public String getMessage() {
        return msg.toJSONString();
    }

    /**
     * Generates an MD5 hash using the bytes from the hostId and the message
     * body.
     *
     * @param msg   The message body
     * @return      MD5 hash of the hostId and msg body in hex format
     */
    private String generateMessageID(String msg) {
        MessageDigest md = null;
        StringBuffer sb = null;

        try {
            md = MessageDigest.getInstance("MD5");
            md.update((hostId + msg).getBytes());

            // Source: http://www.mkyong.com/java/java-md5-hashing-example/
            byte data[] = md.digest();

            sb = new StringBuffer();
            for (int i = 0; i < data.length; i++) {
                sb.append(Integer.toString((data[i] & 0xff) 
                            + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException nsae) {
        }

        return sb.toString().toUpperCase();
    }

}
