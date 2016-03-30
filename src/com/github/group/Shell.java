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
    private Group lastGroupMessaged;

    /**
     * Constructor
     */
    protected Shell( NodeServer ns)
    {
    }


    public void run()
    {
        System.out.println("Shell started with username: " + P2PChat.username);
        Scanner s = new Scanner(System.in);

        String input = "";
        while ((input = s.nextLine())!= null)
        {
            String[] splitArray = input.split(" ",3);
            System.out.println("sa length" + splitArray.length);

            switch (splitArray[0])
            {
                // join/create group
                case "/j":
                    if (splitArray.length > 1)
                    {
                        boolean newGroup = false;
                        Group g = GroupList.getInstance().getGroup(splitArray[1]);



                        if (g == null)
                        {
                            // create new group if it does not exist
                            g = new Group(splitArray[1], "new group name", P2PChat.username); // TODO use ID P2PChat.id);
                            GroupList.getInstance().addGroup(g);

                            Join body = new Join(g);
                            ControlMessage joinMsg = new ControlMessage();
                            joinMsg.setMsgBody(body.toJsonString());

                            PeerList.messageAllPeers(joinMsg);
                        } else {
                            // send dumpReq to external contact if group is already there
                            DumpReq dBody = new DumpReq(g);
                            ControlMessage dumpMessage = new ControlMessage();
                            dumpMessage.setMsgBody(dBody.toJsonString());
//                            System.out.println(g.getExternalContact());
                            PeerList.getPeerByName(g.getExternalContact()).sendMessage(dumpMessage);
                        }
                        // send join/create message to every peer, groupManager will handle replication of peerlists on their side
                        }
                    break;

                // leave group
                case "/l":
                    if (splitArray.length > 1)
                    {
                        Group g = GroupList.getInstance().getGroup(splitArray[1]);
                        Leave leaveBody = new Leave(g);
                        ControlMessage leaveMsg = new ControlMessage();
                        leaveMsg.setMsgBody(leaveBody.toJsonString());
                        g.messageGroup(leaveMsg);
                        g.clearGroup();
                    }
                    break;

                // show online peers
                case "/p":
                    //PeerList.displayPeerList();
                    System.out.println("Listing all peers :");
//                    PeerList.displayPeerListUsernames();
                    PeerList.displayPeerList();
                    System.out.println("-------------------");
                    break;

                // show all groups on network
                case "/e":
                    //PeerList.displayPeerList();
                    System.out.println("Listing all Groups on network :");
                    GroupList.getInstance().displayGroupList();
                    System.out.println("-------------------");
                    break;

                // show members of a group that i am a part of
                case "/m":
                    /*
                    if (splitArray.length > 2)
                    {
                        Group g = GroupList.getInstance().getGroup(splitArray[1]);

                    }*/
                    break;

                // group message
                case "/g":
                    if (splitArray.length > 2)
                    {
                        lastGroupMessaged = GroupList.getInstance().getGroup(splitArray[1]);
                        lastGroupMessaged.messageGroup(splitArray[2]);
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
                            System.out.println( p.toJsonString());

                            m.setDst(p.ip, p.port);
                            m.setMsgBody(splitArray[2]);
                            m.signMessage();
                            System.out.println("shell sending chat msg");
                            p.sendMessage(m);
                            System.out.println("shell sent chat msg");


                            /*
                            Socket conn = lastPeerMessaged.getConn();
                            try {
                                PrintWriter out = new PrintWriter(conn.getOutputStream(), true);
                                out.println(m.toJsonString());
                                out.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }*/
                        } else
                        {
                            System.out.println("no such peer exists");
                        }
                    } else
                    {
                        System.out.println("Improper input");
                    }
                    break;

                case "/c":
                    Peer p = PeerList.getPeerByName(splitArray[1]);
                    if (p != null) {
                        Message m = new Message(MessageType.CONTROL);
                        p.sendMessage(m);
                    }
                    break;
                default:
                    if(lastGroupMessaged != null)
                        lastGroupMessaged.messageGroup(input);
            }
        }

    }


}
