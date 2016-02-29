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
     * @return
     */
    public String getStamp() {
        return timestamp;
    }

    /**
     * @return
     */
    public String getHostID() {
        return hostId;
    }

    /**
     * @return
     */
    public String getGroupID() {
        return groupId;
    }

    /**
     * @return
     */
    public String getMessageID() {
        return messageId;
    }

    /**
     * @return
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * @return
     */
    public String getMessageBody() {
        return messageBody;
    }

    /**
     * @return
     */
    public String ControlMsg() {
        return msg.toJSONString();
    }

    /**
     * @return
     */
    private String generateMessageID(String msg) {
        MessageDigest md = null;
        StringBuffer sb = null;

        try {
            md = MessageDigest.getInstance("SHA");
            md.update(msg.getBytes());

            // Source: http://www.mkyong.com/java/java-md5-hashing-example/
            byte data[] = md.digest();

            sb = new StringBuffer();
            for (int i = 0; i < data.length; i++) {
                sb.append(Integer.toString((data[i] & 0xff) 
                            + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException nsae) {
        }

        return sb.toString();
    }

}
