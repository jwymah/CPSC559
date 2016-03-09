/**
 *  GroupMessage.java
 *
 *  @author Cory Hutchison
 *  @author Frankie Yuan
 *  @author Jeremy Mah
 */
package com.github.group;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.simple.JSONObject;

		
public class GroupMessage extends Message {

    private final static String CLASS_ID = "GroupMessage";
    private static Log log;
    private JSONObject msg;
    
    private String dst;
	private int port;

    /**
     * Constructor
     */
    public GroupMessage() {
        super();
        log = Log.getInstance();

        // Package in JSON object
        msg = new JSONObject();
        msg.put("TimeStamp", super.timestamp);
        msg.put("src", "SRC");
        msg.put("dst", "IP:PORT");
        msg.put("dstid", "DST");
        msg.put("msgsig", "MSGSIG");
        msg.put("msgbody", "MSGBODY");

        populateSrc();
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
	
	public void signMessage()
	{
		//TODO hash of msgBody + src
		msg.put("msgsig", "1111");
	}
	
	public void send()
	{
		try
		{
			while(true)
			{
				Socket socket = new Socket(dst, port);
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				signMessage();
				out.writeChars(msg.toString());
				socket.shutdownOutput();
				if (in.read() == 0) //does this actually happen?
				{
					break;
				}
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
