package com.github.group;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.simple.JSONArray;

public class GroupList
{
	private static final String CLASS_ID = "GroupList";
	private Map<String, Group> groups;
	private Set<Group> myGroups;
	private static Log log = Log.getInstance();

    private static GroupList instance = null;
	
    /**
     * Constructor
     */
	protected GroupList()
	{
		groups = new HashMap<String, Group>();
		myGroups = new HashSet<Group>();
	}

    /**
     * Returns an instance of GroupList
     * 
     * @return An instance of the GroupList
     */
    public static GroupList getInstance() {

        if (instance == null) {
            instance = new GroupList();
        }
        return instance;
    }
    
    /**
     * @param g Group
     */
    public synchronized void imJoiningGroup(Group g)
    {
    	myGroups.add(g);
    }
    
    /**
     * @param g Group
     */
    public synchronized void imLeavingGroup(Group g)
    {
    	myGroups.remove(g);
    }

    /**
     * @return a copy of all groups this peer is a member of
     */
    public synchronized Set<Group> getMyGroups()
    {
    	Set<Group> copy = new HashSet<Group>();
    	copy.addAll(myGroups);
    	return copy;
    }

    /**
     * @param g Group
     * @return true if member of group, false otherwise
     */
    public synchronized boolean amIInGroup(Group g)
    {
    	return myGroups.contains(g);
    }

    /**
     * @param groupToAdd the group to add
     */
	public synchronized void addGroup(Group groupToAdd)
	{
		if (groups.get(groupToAdd.getId()) != null)
		{
            log.printLogMessage(Log.ERROR, CLASS_ID, "ID already exists in GroupList");
			return;
		}
		groups.put(groupToAdd.getId(), groupToAdd);
	}

    /**
     * @param groupID the group id lookup
     * @return the matching group with the groupID
     */
	public synchronized Group getGroup(String groupID)
	{
		if(!groups.containsKey(groupID))
            return null;
		return groups.get(groupID);
	}

    /**
     * @param groupToRemove the group to be removed
     */
	public synchronized void removeGroup(Group groupToRemove)
	{
		groups.remove(groupToRemove);
	}

	/**
	 * get meta data for all groups
	 * @return JSONArray of groups meta data
	 * 	["groupId,groupName,externalContact","groupId,groupName,externalContact",...]
	 */
	public synchronized JSONArray getAllMetadata()
	{
		JSONArray array = new JSONArray();
		for (Group g : groups.values())
		{
			array.add(g.getMetadata());
		}
		return array;
	}
	
    /**
     * @param peer the peer to remove
     */
	public synchronized void removePeerFromAllGroups(Peer peer)
	{
		for (Group g : groups.values())
		{
			g.removePeer(peer);
		}
	}
	
    /**
     * Displays all groups by id in the GroupList
     */
    public synchronized void displayGroupList()
    {
        log.printLogMessage(Log.INFO, CLASS_ID, "Displaying Groups");
        System.out.println();
        for (Entry e: groups.entrySet())
        {
            Group g = (Group) e.getValue();

            System.out.println("\tID:\t\t" + e.getKey());
            System.out.println("\tName:\t\t" + g.getName()); 
            System.out.println("\tContact:\t" + g.getExternalContact());
            System.out.println("\tSize:\t\t" + g.size());
            System.out.println();
        }
    }
}
