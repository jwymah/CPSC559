package com.github.group;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import controlMessages.ControlMessage;
import controlMessages.DumpReq;
import controlMessages.DumpResp;
import controlMessages.Join;
import controlMessages.Leave;
import controlMessages.MetadatasDump;
import controlMessages.ControlAction;

public class GroupManager
{
    private static Log log = Log.getInstance();
	private static final String CLASS_ID = "GroupManager";
    private static JSONParser parser = new JSONParser();
    private static GroupList grouplist = GroupList.getInstance();

	public static void handleControlMessage(Peer peer, ControlMessage controlMsg)
	{
        log.printLogMessage(Log.INFO, CLASS_ID, "Handling Control Message");
        controlMsg.printMessage();
		//System.out.println(controlMsg.toJsonString());
        // System.out.println(parseAction(controlMsg.getMsgBody()));
		
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
					targetGroup = new Group(j.getTargetGroup(), 
                            j.getGroupName(), j.getExternalContact());
					GroupList.getInstance().addGroup(targetGroup);
				}
				targetGroup.addPeer(peer);
				break;

			case METADATA:
				//respond with known group metadata
				ControlMessage metaDumpMsg = new ControlMessage();
				metaDumpMsg.setMsgBody(new MetadatasDump().toJsonString());
				peer.sendMessage(metaDumpMsg);
				break;
				
			case METADATADUMP:
				//unpack metadatadump and add members to groups
				MetadatasDump md = new MetadatasDump(msgBody);
				for (int i=0; i<md.getMetadatas().size(); i++)
				{
					JSONObject ob = (JSONObject) md.getMetadatas().get(i);
					GroupList.getInstance().addGroup(new Group((String) ob.get("id"), 
                                (String) ob.get("groupname"), 
                                (String) ob.get("externalcontact")));
                    // GroupList.getInstance().displayAllGroups();
				}
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
				
			case DUMPREQ:
				DumpReq d = new DumpReq(msgBody);
				targetGroup = grouplist.getGroup(d.getTargetGroup());
				
				if (targetGroup != null)
				{
					DumpResp dumpRespBody = new DumpResp(targetGroup);
					ControlMessage dumpRespMsg = new ControlMessage();
					dumpRespMsg.setMsgBody(dumpRespBody.toJsonString());
					
					peer.sendMessage(dumpRespMsg);
				}
				break;

			case DUMPRESP:
				DumpResp dr = new DumpResp(msgBody);
				targetGroup = grouplist.getGroup(dr.getTargetGroup());
				// assumed that the targetgroup does not equate to a null group on this side
				targetGroup.addMemberDump(dr.getMemberDump());

				Join body = new Join(targetGroup);
				ControlMessage joinMsg = new ControlMessage();
				joinMsg.setMsgBody(body.toJsonString());

				targetGroup.messageGroup(joinMsg);
				break;
				
			default:
                log.printLogMessage(Log.ERROR, CLASS_ID, "Invalid GroupManager action");
				//System.out.println("hi  ----  Got a weird action in group manager");
		}
	}

    /**
     * @param messageBody The incoming message in JSON formatted string
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
