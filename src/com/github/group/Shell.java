/**
 * Shell.java
 *
 * @author Cory Hutchison
 * @author Frankie Yuan
 * @author Jeremy Mah
 */
package com.github.group;

import controlMessages.ControlMessage;
import controlMessages.DumpReq;
import controlMessages.Join;
import controlMessages.Leave;

import java.util.Scanner;

public class Shell extends Thread
{
    private final static String CLASS_ID = "Shell";
    private Group lastGroupMessaged;
    private static Log log = null;

    /**
     * Constructor
     */
    protected Shell( NodeServer ns)
    {
        log = Log.getInstance();
    }

    public void usage() {
        System.out.println();
        System.out.println("Usage:");
        System.out.println("\t/p\t\t\tDisplay available Peers");
        System.out.println("\t/e\t\t\tDisplay available Groups");
        System.out.println("]t/m\t\t\tDisplay groups I am a part of");
        System.out.println("\t/gm <group>\t\tDisplay known members of a group");
        System.out.println("\t/g <group> <msg>\tWrite group message");
        System.out.println("\t/j <group>\t\tJoin/Create group");
        System.out.println("\t/l <group>\t\tLeave group");
        System.out.println("\t/w <username> <msg>\tWrite message to peer");
        System.out.println("\t/?\t\t\tDisplay this help message");
        System.out.println();
    }

    public void run()
    {
        log.printLogMessage(Log.INFO, CLASS_ID,
                "Shell started with username: " +
                P2PChat.username);
        Scanner s = new Scanner(System.in);

        String input = "";
        while ((input = s.nextLine())!= null)
        {
            try
			{
				String[] splitArray = input.split(" ",3);

				switch (splitArray[0])
				{
				    // usage
				    case "/?":
				        usage();
				        break;

				    // join/create group
				    case "/j":
				        if (splitArray.length > 1)
				        {
				            Group g = GroupList.getInstance().getGroup(splitArray[1]);

				            if (g == null)
				            {
				                // create new group if it does not exist
				                // TODO: Use ID as P2PChat.id
				                g = new Group(splitArray[1], "new group name", P2PChat.id);
				                GroupList.getInstance().addGroup(g);

				                Join body = new Join(g, true);
				                ControlMessage joinMsg = new ControlMessage();
				                joinMsg.setMsgBody(body.toJsonString());

				                PeerList.messageAllPeers(joinMsg);
				            } else {
				                // send dumpReq to external contact if 
				                // group is already there
				                DumpReq dBody = new DumpReq(g);
				                ControlMessage dumpMessage = new ControlMessage();
				                dumpMessage.setMsgBody(dBody.toJsonString());
				                if (g.getExternalContact().compareTo(P2PChat.id) != 0)
				                {
				                	PeerList.getPeerById(g.getExternalContact()).sendMessage(dumpMessage);
				                }
				            }
			                GroupList.getInstance().imJoiningGroup(g);
				        }
				        break;

				    // leave group
				    case "/l":
				        if (splitArray.length > 1)
				        {
				        	//if there are other members, send to group
				        	//if there are no other members, send to everyone (disband group)
				            Group g = GroupList.getInstance().getGroup(splitArray[1]);
				            Leave leaveBody = new Leave(g);
				            ControlMessage leaveMsg = new ControlMessage();
				            leaveMsg.setMsgBody(leaveBody.toJsonString());
				            if(g.size() > 0)
				            {
				            	g.messageGroup(leaveMsg);
				            }
				            else
				            {
				            	g.clearGroup();
				            	PeerList.messageAllPeers(leaveMsg);
				            }
			                GroupList.getInstance().imLeavingGroup(g);
				        }
				        break;

				    // show online peers
				    case "/p":
				        PeerList.displayPeerList();
				        break;

				    // show all groups on network
				    case "/e":
				        GroupList.getInstance().displayGroupList();
				        break;

				    // show members of a group that i am a part of
				    case "/m":
				    	System.out.println("You are in [" + GroupList.getInstance().getMyGroups().size() + "] groups:");
				    	int index = 0;
				    	for (Group g : GroupList.getInstance().getMyGroups())
				    	{
				    		System.out.println("\t" + index + ": " + g.getId());
				    		index++;
				    	}
				        break;

				    // group message
				    case "/g":
				        if (splitArray.length > 2)
				        {
				            lastGroupMessaged = GroupList.getInstance().getGroup(splitArray[1]);
				            lastGroupMessaged.messageGroup(splitArray[2]);
				        }
				        break;
				    
				    // display known members of a group
				    // ie. /gm 123
				    case "/gm":
				    	if (splitArray.length > 1)
				    	{
				    		Group g = GroupList.getInstance().getGroup(splitArray[1]);
				    		if(g != null)
				    		{
				    			String[] groupMembersArray = GroupList.getInstance().getGroup(splitArray[1]).getMemberNames();
					    		System.out.println(String.join(", ", g.getMemberNames()));
				    		}
				    		else
				    		{
				    			System.out.println("No group found with id: " + splitArray[1]);
				    		}
				    	}
				        break;

				    // whisper one peer
				    case "/w":
				        if (splitArray.length > 2)
				        {
				            Peer p = PeerList.getPeerByName(splitArray[1]);
				            ChatMessage m = new ChatMessage();

				            if (p != null)
				            {
				                m.setDst(p.ip, p.port);
				                m.setDstId(splitArray[1]);
				                m.setMsgBody(splitArray[2]);
				                m.signMessage();
				                
				                p.sendMessage(m);
				            } 
				            else
				            {
				                log.printLogMessage(Log.ERROR, CLASS_ID, "No such peer");
				            }
				        } else
				        {
				            log.printLogMessage(Log.ERROR, CLASS_ID, "Invalid input");
				        }
				        break;
				    default:
				        log.printLogMessage(Log.ERROR, CLASS_ID, "Unrecognized command");
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
        }
        s.close();
    }
}
