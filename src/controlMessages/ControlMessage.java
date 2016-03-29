/**
 *  ControlMessage.java
 *
 */
package controlMessages;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.github.group.Log;
import com.github.group.Message;
import com.github.group.MessageServer;
import com.github.group.MessageType;

		
public class ControlMessage extends Message {

    private final static String CLASS_ID = "ControlMessage";
    private static Log log;
    private JSONObject msg;

	private String src;
    private String dst;
	private String dstid;
	private String msgsig;
	private String msgbody;
//	private ControlType controlType;
	private int port;

    /**
     * Constructor
     */
    public ControlMessage() {
        super(MessageType.CONTROL);
        log = Log.getInstance();

        // Package in JSON object
        msg = new JSONObject();
        msg.put("timestamp", super.timestamp);
        msg.put("type", super.type.toString());
        msg.put("src", "SRC");
        msg.put("dst", "IP:PORT");
        msg.put("dstid", "DSTID");
        msg.put("msgsig", "MSGSIG");
        msg.put("msgbody", "MSGBODY");

        populateSrc();
    }    

    /**
     * Constructor that parses and input message
     */
    public ControlMessage(String m) {
    	super(MessageType.CONTROL);

        // Remove weird added whitespace that rekt parsing
        // and initialize JSON parser
        m = m.trim();   
        JSONParser parser = new JSONParser();

        // Parse message and get message components
        try {
            Object obj = parser.parse(m);

            msg = (JSONObject) obj;
            timestamp = (long) msg.get("timestamp");
            type = MessageType.CONTROL;
            src = (String) msg.get("src");
            dst = (String) msg.get("dst");
            dstid = (String) msg.get("dstid");
            msgsig = (String) msg.get("msgsig");
            msgbody = (String) msg.get("msgbody");
            
            
            /**{"dst":"IP:PORT",
             * "src":"192.168.0.19:0",
             * "msgsig":"MSGSIG",
             * "dstid":"DSTID",
             * "type":"control",
             * "msgbody":"{\"targetgroupid\":\"12\",\"externalcontactid\":\"662af6ed167fab65bb5049216a980d1419425f75\",\"action\":\"join\",\"groupname\":\"helluva group\"}",
             * "timestamp":1458718049}
             */
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

	public void setMsgBody(String msgBody)
	{
		msg.put("msgbody", msgBody);
	}
	
	public String getMsgBody()
	{
		return (String) msg.get("msgbody");
	}
	
//	public ControlType getControlType()
//	{
//		return controlType;
//	}
	
	public void signMessage()
	{
		//TODO hash of msgBody + src + timestamp + controlType
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