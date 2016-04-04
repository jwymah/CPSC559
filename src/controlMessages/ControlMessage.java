/**
 *  ControlMessage.java
 *
 */
package controlMessages;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.github.group.Log;
import com.github.group.Message;
import com.github.group.MessageType;
import com.github.group.P2PChat;

		
public class ControlMessage extends Message {

    private final static String CLASS_ID = "ControlMessage";
    private static Log log;
    private JSONObject msg;

	private String src;
    private String dst;
	private String msgsig;
	private String msgbody;

    /**
     * Constructor
     */
    public ControlMessage() {
        super(MessageType.CONTROL);
        log = Log.getInstance();

        // Package in JSON object
        msg = new JSONObject();
        msg.put("timestamp", super.timestamp);
        msg.put("type", MessageType.CONTROL.toString());
        msg.put("src", src);
        msg.put("dst", dst);
        msg.put("msgsig", msgsig);
        msg.put("msgbody", msgbody);
        
        setSrc();
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
            msgsig = (String) msg.get("msgsig");
            msgbody = (String) msg.get("msgbody");
            
            
            /**
             * {"dst":"IP:PORT",
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

	public void setSrc()
	{
		msg.put("src", P2PChat.username);
	}

	@Override
	public void setDst(String username)
	{
		dst = username;
		msg.put("dst", dst);
	}

	public void setMsgBody(String msgBody)
	{
		msg.put("msgbody", msgBody);
	}
	
	public String getMsgBody()
	{
		return (String) msg.get("msgbody");
	}
	
	public void signMessage()
	{
		//TODO hash of msgBody + src + timestamp + controlType
		msgsig = "11111";
	}

    /**
     * {
     *      "timestamp":1458718049}
     *      "type":"control",
     *      "src":"192.168.0.19:0",
     *      "dstid":"DSTID",
     *      "dst":"IP:PORT",
     *      "msgsig":"MSGSIG",
     *      "msgbody": "{\"targetgroupid\":\"12\",\"externalcontactid\":\"662af6ed167fab65bb5049216a980d1419425f75\",\"action\":\"join\",\"groupname\":\"helluva group\"}",
     */
    public void printMessage() {
        System.out.println();
        System.out.println("{");
        System.out.println("\t\"timestamp\": " + timestamp);
        System.out.println("\t\"type\": \"" + type + "\"");
        System.out.println("\t\"src\": \"" + src + "\"");
        System.out.println("\t\"dst\": \"" + dst + "\"");
        System.out.println("\t\"msgsig\": \"" + msgsig + "\"");
        System.out.println("\t\"msgbody\": \"" + msgbody + "\"");
        System.out.println("}");
        System.out.println();
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
