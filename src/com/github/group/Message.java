/**
 *  Message.java
 *
 *  @author  Cory Hutchison
 *  @author  Jeremy Mah
 *  @author  Frankie Yuan
 */ 
package com.github.group;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Message {

    private final static String CLASS_ID = "Message";
    private static Log log;
    private static JSONParser parser = new JSONParser();

    public long timestamp;
    public MessageType type = MessageType.BLANK;

    /**
     * Constructor
     */
    public Message(MessageType type) {

        // Get instance of log
        log = Log.getInstance();

        // Give message a UNIX Timestamp 
        // NOTE: Attempted on both Unix and Windows with success
        timestamp = (long) (System.currentTimeMillis() / 1000L);
        this.type = type;
    }
    
    /**
     * Constructor that parses and input message
     */
    public Message(String m) {
        // Remove weird added whitespace that rekt parsing
        // and initialize JSON parser
        m = m.trim();   

        // Parse message and get message components
        try {
            Object obj = parser.parse(m);

            JSONObject msg = (JSONObject) obj;

            timestamp = (long) msg.get("TimeStamp");
            
        } catch (ParseException e) {
            log.printLogMessage(Log.ERROR, CLASS_ID, "Received invalid BroadcastMessage");
            System.out.println(m);
        }
    }
    
    /**
     * @param jsonMessageString The incoming message in JSON formatted string
     */
    public static MessageType parseMessageType(String jsonMessageString)
    {
    	MessageType type = null;
    	jsonMessageString = jsonMessageString.trim();
    	
    	try
		{
			JSONObject msg = (JSONObject) parser.parse(jsonMessageString);
			return (MessageType.fromString(msg.get("type").toString()));				
		}
		catch (ParseException e)
		{
			e.printStackTrace();
			log.printLogMessage(Log.ERROR, CLASS_ID, "Received message with no Type field");
		}
    	return type;
    }

    /**
     * Returns a Message as JSON in a string format
     *
     * @return JSON Message as string
     */
    public String toJsonString()
    {
    	JSONObject msg = new JSONObject();
        msg.put("TimeStamp", timestamp);
        msg.put("type", type.toString());
        return msg.toJSONString();
    }
}
