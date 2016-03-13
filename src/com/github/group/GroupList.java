package com.github.group;

import java.util.HashMap;
import java.util.Map;

import com.sun.java.swing.plaf.motif.MotifCheckBoxMenuItemUI;

public class GroupList
{

//  msg = new JSONObject();
//  msg.put("TimeStamp", super.timestamp);
//  msg.put("src", "SRC");
//  msg.put("dst", "IP:PORT");
//  msg.put("dstid", "DST");
//  msg.put("msgsig", "MSGSIG");
//  msg.put("msgbody", "MSGBODY");
	
	private Map<String, Group> groups;
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

	public void addGroup(Group groupToAdd)
	{
		groups.put(groupToAdd.getId(), groupToAdd);
	}

	public Group getGroup(String groupID)
	{
		return groups.get(groupID);
	}

	public void removeGroup(String groupID)
	{
		
	}
	
	public void mockMessageGroup(String msg)
	{
		Group mocky = getGroup("1");
		addAllPeersToGroup(mocky);
		mocky.messageGroup(msg);
	}
	
	public void addAllPeersToGroup(Group group)
	{
		for (Peer e : PeerList.getInstance().getAllPeers())
		{
			group.addPeer(e);
		}
	}
}