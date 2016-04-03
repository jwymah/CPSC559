package com.github.group;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONArray;

public class GroupList
{
	private static final String CLASS_ID = "GroupList";
	private Map<String, Group> groups;
	private static Log log = Log.getInstance();

    private static GroupList instance = null;
	
	protected GroupList()
	{
		groups = new HashMap<String, Group>();
		//groups.put("test", new Group());
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

	public synchronized void addGroup(Group groupToAdd)
	{
		if (groups.get(groupToAdd.getId()) != null)
		{
			//TODO change to logger
            log.printLogMessage(Log.ERROR, CLASS_ID, "ID already exists in GroupList");
			//System.out.println("tried to add a group to grouplist but the ID already exists");
			return;
		}
		groups.put(groupToAdd.getId(), groupToAdd);
	}

	public synchronized Group getGroup(String groupID)
	{
		if(!groups.containsKey(groupID))
            return null;
		return groups.get(groupID);
	}

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
	
	public void mockMessageGroup(String msg)
	{
		Group mocky = getGroup("test");
		addAllPeersToGroup(mocky);
		mocky.messageGroup(msg);
	}
	
	/**
	 * mock testing method that inserts all known peers into the provided group
	 * @param group
	 */
	public void addAllPeersToGroup(Group group)
	{
		for (Peer e : PeerList.getInstance().getAllPeers())
		{
			group.addPeer(e);
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
            /*
            System.out.print("ID: " + e.getKey());
			System.out.println(", name: " + g.getName() + ", size: " + g.size() + ", externalContact: " + g.getExternalContact());
            */

        }
    }
}
