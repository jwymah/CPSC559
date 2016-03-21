/**
 *  ChatMessage.java
 *
 *  @author Cory Hutchison
 *  @author Frankie Yuan
 *  @author Jeremy Mah
 */
package com.github.group;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

		
public class ChatMessage extends Message {

    private final static String CLASS_ID = "ChatMessage";
    private static Log log;
    private JSONObject msg;

	private String src;
    private String dst;
	private String dstid;
	private String msgsig;
	private String msgbody;
	private String grpid;
	private int port;

    /**
     * Constructor
     */
    public ChatMessage() {
        super(MessageType.CHAT);
        log = Log.getInstance();

        // Package in JSON object
        msg = new JSONObject();
        msg.put("TimeStamp", super.timestamp);
        msg.put("type", super.type.toString());
        msg.put("src", "SRC");
        msg.put("dst", "IP:PORT");
        msg.put("dstid", "DSTID");
        msg.put("msgsig", "MSGSIG");
		msg.put("msgbody", "MSGBODY");
		msg.put("grpid", "GROUPID");

        populateSrc();
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
            timestamp = (long) msg.get("TimeStamp");
            type = super.type;
            src = (String) msg.get("src");
            dst = (String) msg.get("dst");
            dstid = (String) msg.get("dstid");
            msgsig = (String) msg.get("msgsig");
            msgbody = (String) msg.get("msgbody");
			grpid = (String) msg.get("grpid");
        } catch (ParseException e) {
            log.printLogMessage(Log.ERROR, CLASS_ID, "Received invalid ChatMessage");
            System.out.println(m);
        }
    }

	private void populateSrc()
	{
		try
		{
			msg.put("src", InetAddress.getLocalHost().getHostAddress() + ":" + new Integer(MessageServer.getPort()).toString());
		}
		catch (UnknownHostException e)
		{
			//TODO: probably want this fault to propagate
			e.printStackTrace();
		}
	}

	public void setDst(String ip, int port)
	{
		dst = ip;
		this.port = port;
		msg.put("dst", ip + new Integer(port).toString());
	}

	public void setGroup(String GroupID)
	{
		msg.put("grpid",GroupID);
	}

	public void setMsgBody(String msgBody)
	{
		msg.put("msgbody", msgBody);
	}

    public String getMsgBody()
    {
        return (String) msg.get("msgbody");
    }

    public String getMsgGroup()
    {
        return (String) msg.get("grpid");
    }
	
	public void signMessage()
	{
		//TODO hash of msgBody + src
		msg.put("msgsig", "1111");
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
}
