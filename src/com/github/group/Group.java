/**
 * Group.java
 *
 * @author Cory Hutchison
 * @author Frankie Yuan
 * @author Jeremy Mah
 */
package com.github.group;

import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Group {

    private Set<Peer> groupMembers;
    private String id;
    private String groupName;
    private String externalContact;

    /**
     * Constructor
     */
    public Group() {
    	id = "1"; // TODO: be dynamic
        groupMembers = new HashSet<Peer>();
    }
    
    public Group(String groupId, String groupName, String externalContact)
	{
    	groupMembers = new HashSet<Peer>();
    	id = groupId;
    	this.groupName = groupName;
    	this.externalContact = externalContact;
	}

	public void messageGroup(String msgBody)
    {
    	ChatMessage msg = new ChatMessage();
    	for(Peer p : groupMembers)
    	{
    		msg.setDst(p.ip, p.port);
    		msg.setMsgBody(msgBody);
    		msg.signMessage();

            p.sendMessage(msg);
            /*
            Socket conn = p.getConn();


            //TODO: have spin up a SINGLE THREAD that handles sending over each socket. don't want messages being interleaved
            try {
//                PrintWriter out = new PrintWriter(conn.getOutputStream(), true);
//                out.println(msg.toJsonString());
            	p.sendMessage(msg);
            }
            catch(Exception ex)
            {
            	ex.printStackTrace();
            }*/
    	}
    }

    /**
     * overload function
     * mainly used to send control messages
     * @param msg
     */
    public void messageGroup(Message msg)
    {
        for (Peer p : groupMembers)
        {
            p.sendMessage(msg);
        }
    }
    
    public void updateGroupStatus()
    {
//    	ControlMessage msg = new ControlMessage();
    	Message msg = new Message(MessageType.CONTROL); //TODO: change this to ControlMessage, a child of Message
		for(Peer p : PeerList.getAllPeers())
    	{
//    		msg.setDst(p.ip, p.port);
//    		msg.setMsgBody(msgBody);
//    		msg.signMessage();

            //TODO: have spin up a SINGLE THREAD that handles sending over each socket. don't want messages being interleaved
            p.sendMessage(msg);
    	}
    }

    /**
     * adds a Peer from PeerList to this collection.
     * @args Peer - reference to a peer
     */
    public void addPeer(Peer peerToAdd)
    {
    	groupMembers.add(peerToAdd);
    }

	public void addMemberDump(JSONArray memberDump)
	{
		for(int i=0; i<memberDump.size(); i++)
		{
			JSONObject ob = (JSONObject) memberDump.get(i);
			PeerList.getPeer((String) ob.get("id"));
		}
	}
    
    public void removePeer(Peer peerToRemove)
    {
    	groupMembers.remove(peerToRemove);
    }
    
    public String getId()
    {
    	return id;
    }
    
    public String getName()
    {
    	return groupName;
    }
    
    public String getExternalContact()
    {
    	return externalContact;
    }
    
    public int size()
    {
    	return groupMembers.size();
    }
    
    /**
     * get shallow information about the group: id, name, and external contact
     * @return JSONString
        {"id": id, "groupname": groupName, "externalcontact": externalContact};
     */
    public String getMetadata()
    {
        JSONObject details = new JSONObject();
        details.put("id", id);
        details.put("groupname", groupName);
        details.put("externalcontact", externalContact);
        
        return details.toJSONString();
    }

    /**
     * clears all members from this group
     */
    public void clearGroup()
    {
        groupMembers = new HashSet<Peer>();
    }

    /**
     * get Ids of all members of this group
     * @return
     */
	public String[] getMembersIds()
	{
		String[] ids = new String[groupMembers.size()];
		int index = 0;
		for(Peer p : groupMembers)
		{
			ids[index] = p.id;
			index++;
		}
		return ids;
	}
}