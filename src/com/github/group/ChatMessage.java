/**
 *  ChatMessage.java
 *
 *  @author Cory Hutchison
 *  @author Frankie Yuan
 *  @author Jeremy Mah
 */
package com.github.group;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

		
public class ChatMessage extends Message {

    private final static String CLASS_ID = "ChatMessage";
    private static Log log;
    private JSONObject msg;

    private String groupid;
	private String src;
    private String dst;
	private String msgsig;
	private String msgbody;
	private int port;

    /**
     * Constructor
     */
    public ChatMessage() {
        super(MessageType.CHAT);
        log = Log.getInstance();

        // Package in JSON object
        msg = new JSONObject();
        msg.put("timestamp", super.timestamp);
        msg.put("type", MessageType.CHAT.toString());
        msg.put("groupid", "-1");
        msg.put("src", "SRC");
        msg.put("dst", "DST");
        msg.put("msgsig", "MSGSIG");
        msg.put("msgbody", "MSGBODY");

        setSrc();
    }    

    /**
     * Constructor that parses and input message
     */
    public ChatMessage(String m) {
    	super(MessageType.CHAT);

        // Remove weird added whitespace that rekt parsing
        // and initialize JSON parser
        m = m.trim();   
        JSONParser parser = new JSONParser();

        // Parse message and get message components
        try {
            Object obj = parser.parse(m);

            msg = (JSONObject) obj;
            timestamp = (long) msg.get("timestamp");
            type = super.type;
            groupid = (String) msg.get("groupid");
            src = (String) msg.get("src");
            dst = (String) msg.get("dst");
            msgsig = (String) msg.get("msgsig");
            msgbody = (String) msg.get("msgbody");
        } catch (ParseException e) {
            log.printLogMessage(Log.ERROR, CLASS_ID, "Received invalid ChatMessage");
            //System.out.println(m);
        }
    }

	private void setSrc()
	{
		msg.put("src", P2PChat.username);
	}
	
	/**
	 * Set the groupid on messages when messaging a group so that the client side parser
	 * knows that it was a group message.
	 * groupid is defaulted to -1 in the case that it a direction 1-1 message.
	 * @param groupid
	 */
	public void setGroupId(String groupid)
	{
		msg.put("groupid", groupid);
	}

	public void setDst(String ip, int port)
	{
		dst = ip;
		this.port = port;
		msg.put("dst", ip + new Integer(port).toString());
	}

    public void setDstId(String id) {
        msg.put("dstid", id);
    }

	public void setMsgBody(String msgBody)
	{
		msg.put("msgbody", msgBody);
	}

	public String getMsgBody()
	{
		return (String) msg.get("msgbody");
	}
	public String getSender()
	{
		return (String) msg.get("src");
	}
	
	public void signMessage()
	{
		// TODO: Hash of msgBody + src
		msg.put("msgsig", "1111");
	}
	
    /**
     * Prints out the Chat message
     */
    public void printMessage() {
        // Set formatting options and get timestamp
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String timestamp = sdf.format(cal.getTime());

        // Print out log message
        if(groupid.compareTo("-1") != 0)
        {
        	System.out.println("[" + timestamp + "] [" + groupid + "] [" + src + "] " + getMsgBody());
        }
        else
    	{
        	System.out.println("[" + timestamp + "] [" + src + "] " + getMsgBody());
    	}
    }

    /**
     * Returns a ChatMessage as JSON in a string format
     *
     * @return JSON ChatMessage as string
     */
	@Override
	public String toJsonString()
	{
		return msg.toJSONString();
	}

	@Override
	public void setDst(String username)
	{
		dst = username;
		msg.put("dst", dst);
	}
}
