package com.github.group;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


import org.json.simple.*;

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

    public String timestamp;
    public String hostId;
    public String groupId;
    public String messageId;
    public String messageType;
    public String messageBody;

    public Message(String id, String type) {
        // Time Stamp
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        timestamp = df.format(date);

        // Host ID
        hostId = id; 

        msg = new JSONObject();
    }

    /**
     * @return
     */
    public String getDate() {
        return timestamp;
    }

    /**
     * @return
     */
    public JSONObject ControlMsg() {
        return msg;
    }

}
