/**
 * Group.java
 *
 * @author Cory Hutchison
 * @author Frankie Yuan
 * @author Jeremy Mah
 */
package com.github.group;

import java.net.Socket;
import java.util.Set;
import java.util.TreeSet;

import org.json.simple.JSONObject;

public class Group {

    public Set<Peer> group;

    /**
     * Constructor
     */
    protected Group() {
        group = new TreeSet<Peer>();
    }

//    msg = new JSONObject();
//    msg.put("TimeStamp", super.timestamp);
//    msg.put("src", "SRC");
//    msg.put("dst", "IP:PORT");
//    msg.put("dstid", "DST");
//    msg.put("msgsig", "MSGSIG");
//    msg.put("msgbody", "MSGBODY");
    public void messageGroup(String msgBody)
    {
    	GroupMessage msg = new GroupMessage();
    	for(Peer e : group)
    	{
    		msg.setDst(e.ip, e.port);
    		//msg.setDstId TODO
    		msg.setMsgBody(msgBody);
    		
    		new MessageSender(msg).run();
    		
    	}
    }
    
    public void updateGroupMembers()
    {
    	//TODO: stub
    }
    
    private class MessageSender extends Thread
    {
    	GroupMessage msg;
    	MessageSender(GroupMessage msg)
    	{
    		this.msg = msg;
    	}
    	
    	public void run()
    	{
    		msg.send();
    	}    	
    }    
}
