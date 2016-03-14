/**
 * Group.java
 *
 * @author Cory Hutchison
 * @author Frankie Yuan
 * @author Jeremy Mah
 */
package com.github.group;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Group {

    private Set<Peer> groupMembers;
    private String id;    

    /**
     * Constructor
     */
    public Group() {
    	id = "1"; // TODO: be dynamic
        groupMembers = new HashSet<Peer>();
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
    	ChatMessage msg = new ChatMessage();
    	for(Peer p : groupMembers)
    	{
    		msg.setDst(p.ip, p.port);
    		msg.setMsgBody(msgBody);
    		msg.signMessage();

            Socket conn = p.getConn();

            //TODO: have spin up a SINGLE THREAD that handles sending over the socket. don't want messages being interleaved
            try {
                PrintWriter out = new PrintWriter(
                        conn.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(
                            conn.getInputStream()));
                out.println(msg.toJsonString());
            }
            catch(Exception ex)
            {
            	ex.printStackTrace();
            }
    	}
    }
    
    public void updateGroupMembers()
    {
    	//TODO: stub
    }

    public void addPeer(Peer peerToAdd)
    {
    	groupMembers.add(peerToAdd);
    }
    
    public String getId()
    {
    	return id;
    }
}