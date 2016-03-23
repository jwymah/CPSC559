package com.github.group;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import controlMessages.ControlMessage;
import controlMessages.Join;
import controlMessages.Leave;
import controlMessages.ControlAction;

public class GroupManager
{
    private static Log log = Log.getInstance();
	private static final String CLASS_ID = "GroupManager";
    private static JSONParser parser = new JSONParser();
    private static GroupList grouplist = GroupList.getInstance();

	public static void handleControlMessage(Peer peer, ControlMessage controlMsg)
	{
		String msgBody = controlMsg.getMsgBody();
		Group targetGroup = null;
		switch (parseAction(msgBody))
		{
			case JOIN:
				// add peer to specified group if exists.
				// otherwise create group with specified contact
				Join j = new Join(msgBody);
				targetGroup = grouplist.getGroup(j.getTargetGroup());
				
				if (targetGroup == null)
				{
					targetGroup = new Group(j.getTargetGroup(), j.getGroupName(), j.getExternalContact());
					GroupList.getInstance().addGroup(targetGroup);
				}

				targetGroup.addPeer(peer);
				
				break;
			case LEAVE:
				// remove said peer from specified group
				Leave l = new Leave(msgBody);
				targetGroup = grouplist.getGroup(l.getTargetGroup());
				
				if (targetGroup != null)
				{
					targetGroup.removePeer(peer);
				}
				
				break;
			case DUMP:
				// return own group list
				break;
				
			default:
				System.out.println("hi  ----  Got a weird action in group manager");
		}
	}

    /**
     * @param jsonMessageString The incoming message in JSON formatted string
     */
    public static ControlAction parseAction(String messageBody)
    {
    	ControlAction type = null;
    	messageBody = messageBody.trim();
    	
    	try
		{
			JSONObject msg = (JSONObject) parser.parse(messageBody);
			return (ControlAction.fromString(msg.get("action").toString()));				
		}
		catch (ParseException e)
		{
			e.printStackTrace();
			log.printLogMessage(Log.ERROR, CLASS_ID, "Received message with no Type field");
		}
    	return type;
    }
}
