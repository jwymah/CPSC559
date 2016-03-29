package com.github.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;

public class GroupList
{	
	private Map<String, Group> groups;
	private static Log log = Log.getInstance();
    private static GroupList instance = null;
	
	protected GroupList()
	{
		groups = new HashMap<String, Group>();
		groups.put("1", new Group());
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
			System.out.println("tried to add a group to grouplist but the ID already exists");
			return;
		}
		groups.put(groupToAdd.getId(), groupToAdd);
	}

	public synchronized Group getGroup(String groupID)
	{
		return groups.get(groupID);
	}

	public synchronized void removeGroup(Group groupToRemove)
	{
		groups.remove(groupToRemove);
	}
	
	public synchronized void displayAllGroups()
	{
		for (Group g : groups.values())
		{
			System.out.println(g.getName() + ":::" + g.size());
		}
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
		Group mocky = getGroup("1");
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
}