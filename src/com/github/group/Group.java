/**
 * Group.java
 *
 * @author Cory Hutchison
 * @author Frankie Yuan
 * @author Jeremy Mah
 */
package com.github.group;

import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import controlMessages.ControlMessage;
import controlMessages.Join;

public class Group {

    private Set<Peer> groupMembers;
    private String id;
    private String groupName;
    private String externalContact;
    private static Log log = null;
    private static final String CLASS_ID = "Group";

    /**
     * Constructor
     *
     * @param groupId The group id
     * @param groupName The group name
     * @param externalContact The external contact of the group
     */
    public Group(String groupId, String groupName, String externalContact)
	{
    	groupMembers = new HashSet<Peer>();
    	id = groupId;
    	this.groupName = groupName;
    	this.externalContact = externalContact;
	}

    /**
     * @param msgBody The message body
     */
	public void messageGroup(String msgBody)
    {
    	ChatMessage msg = new ChatMessage();
		msg.setMsgBody(msgBody);
		
    	for(Peer p : groupMembers)
    	{
    		if (p == null)
                log.printLogMessage(Log.ERROR, CLASS_ID, "This really shouldn't happen");
    		msg.setDst(p.ip, p.port);
    		msg.setGroupId(id);
    		msg.signMessage();

            p.sendMessage(msg);
    	}
    }

    /**
     * overload function
     * mainly used to send control messages
     *
     * @param msg the input message
     */
    public void messageGroup(Message msg)
    {
        for (Peer p : groupMembers)
        {
            p.sendMessage(msg);
        }
    }

    /**
     * Adds a Peer from PeerList to this collection.
     *
     * @param peerToAdd Reference to a peer
     */
    public void addPeer(Peer peerToAdd)
    {
    	groupMembers.add(peerToAdd);
    }

    /**
     * @param memberDump the json information
     */
	public void addMemberDump(JSONArray memberDump)
	{
		for(int i=0; i<memberDump.size(); i++)
		{
        	if (((String) memberDump.get(i)).compareTo(P2PChat.id) == 0)
        		return;	
                // special case if a peer joins same group multiple times. 
                // own ID will be in this dump
			groupMembers.add(PeerList.getPeerById((String) memberDump.get(i)));
		}
	}
    
    /**
     * Removes a peer from the group
     *
     * @param peerToRemove the peer to be removed
     */
    public void removePeer(Peer peerToRemove)
    {
    	groupMembers.remove(peerToRemove);
    	if(getExternalContact().compareTo(peerToRemove.id) == 0)
		{
			reassignExternalContact();
		}
    }

    /**
     * Reassigns the external contact for a group.
     */
	public void reassignExternalContact()
	{
		String newExternal = P2PChat.id;
		for (Peer p : groupMembers)
		{
			if (newExternal.compareTo(p.id) < 0)
			{
				newExternal = p.id;
			}
			externalContact = newExternal;
		}
		// This peer is the new external contact, update everyone including non group members.
		if (newExternal.compareTo(P2PChat.id) == 0)
		{
			externalContact = newExternal;
            Join body = new Join(this, true);
            ControlMessage joinMsg = new ControlMessage();
            joinMsg.setMsgBody(body.toJsonString());

            PeerList.messageAllPeers(joinMsg);
		}
	}
    
    /**
     * @return the group id
     */
    public String getId()
    {
    	return id;
    }
    
    /**
     * @return the group name
     */
    public String getName()
    {
    	return groupName;
    }
    
    /**
     * @return the external contact
     */
    public String getExternalContact()
    {
    	return externalContact;
    }
    
    /**
     * @return the size of a group
     */
    public int size()
    {
    	return groupMembers.size();
    }
    
    /**
     * get shallow information about the group: id, name, and external contact
     * @return JSONSObject
        {"id": id, "groupname": groupName, "externalcontact": externalContact};
     */
    public JSONObject getMetadata()
    {
        JSONObject details = new JSONObject();
        details.put("id", id);
        details.put("groupname", groupName);
        details.put("externalcontact", externalContact);
        
        return details;
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
     *
     * @return Member ids
     */
	public String[] getMembersIds()
	{
		String[] ids = new String[groupMembers.size() + 1];
		int index = 0;
		for(Peer p : groupMembers)
		{
			ids[index] = p.id;
			index++;
		}
		ids[index] = P2PChat.id; 
		return ids;
	}
	
	/**
	 * 
	 * @return array of member names
	 */
	public String[] getMemberNames()
	{
		String[] ids = new String[groupMembers.size() + 1];
		int index = 0;
		for(Peer p : groupMembers)
		{
			ids[index] = p.username;
			index++;
		}
		ids[index] = P2PChat.username; 
		return ids;
	}

    /**
     * Sets the external contact for a group
     * @param externalContact the external contact for the group 
     */
	public void setExternalContact(String externalContact)
	{
		this.externalContact = externalContact;
	}
}
