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
        System.out.println("\t/g <group> <msg>\tWrite group message");
        System.out.println("\t/j <group>\t\tJoin/Create group");
        System.out.println("\t/l <group>\t\tLeave group");
        System.out.println("\t/w <username> <msg>\tWrite message to peer");
        //System.out.println("\t/c <username>\tWrite control message to peer");
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
            String[] splitArray = input.split(" ",3);
            //System.out.println("sa length" + splitArray.length);

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
                        boolean newGroup = false;
                        Group g = GroupList.getInstance().getGroup(splitArray[1]);



                        if (g == null)
                        {
                            // create new group if it does not exist
                            // TODO: Use ID as P2PChat.id
                            g = new Group(splitArray[1], "new group name", P2PChat.username);
                            GroupList.getInstance().addGroup(g);

                            Join body = new Join(g);
                            ControlMessage joinMsg = new ControlMessage();
                            joinMsg.setMsgBody(body.toJsonString());

                            PeerList.messageAllPeers(joinMsg);
                        } else {
                            // send dumpReq to external contact if 
                            // group is already there
                            DumpReq dBody = new DumpReq(g);
                            ControlMessage dumpMessage = new ControlMessage();
                            dumpMessage.setMsgBody(dBody.toJsonString());
                            PeerList.getPeerByName(g.getExternalContact()).sendMessage(dumpMessage);
                        }
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
                    //log.printLogMessage(Log.INFO, CLASS_ID, "Listing Peers");
                    //System.out.println("Listing all peers :");
                    // PeerList.displayPeerListUsernames();
                    PeerList.displayPeerList();
                    //System.out.println("-------------------");
                    break;

                // show all groups on network
                case "/e":
                    //PeerList.displayPeerList();
                    //System.out.println("Listing all Groups on network :");
                    GroupList.getInstance().displayGroupList();
                    //System.out.println("-------------------");
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
                            //System.out.println( p.toJsonString());

                            m.setDst(p.ip, p.port);
                            m.setDstId(splitArray[1]);
                            m.setMsgBody(splitArray[2]);
                            m.signMessage();
                            //System.out.println("shell sending chat msg");
                            p.sendMessage(m);
                            //System.out.println("shell sent chat msg");


                            /*
                            Socket conn = lastPeerMessaged.getConn();
                            try {
                                PrintWriter out = new PrintWriter(conn.getOutputStream(), true);
                                out.println(m.toJsonString());
                                out.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }*/
                        } 
                        else
                        {
                            log.printLogMessage(Log.ERROR, CLASS_ID, "No such peer");
                            //System.out.println("no such peer exists");
                        }
                    } else
                    {
                        log.printLogMessage(Log.ERROR, CLASS_ID, "Invalid input");
                        //System.out.println("Improper input");
                    }
                    break;

                // Control message(?)
                case "/c":
                    Peer p = PeerList.getPeerByName(splitArray[1]);
                    if (p != null) {
                        Message m = new Message(MessageType.CONTROL);
                        p.sendMessage(m);
                    }
                    break;

                default:
                    log.printLogMessage(Log.ERROR, CLASS_ID, "Unrecognized command");
                    /*
                        if(lastGroupMessaged != null)
                            lastGroupMessaged.messageGroup(input);
                    */
            }
        }

    }


}
