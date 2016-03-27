/**
 * Shell.java
 *
 * @author Cory Hutchison
 * @author Frankie Yuan
 * @author Jeremy Mah
 */
package com.github.group;

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
        System.out.println("Shell started");
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
                    break;

                // leave group
                case "/l":
                    break;

                // show online peers
                case "/p":
                    //PeerList.displayPeerList();
                    System.out.println("Listing all peers :");
                    PeerList.displayPeerListUsernames();
                    System.out.println("-------------------");
                    break;

                // show all groups on network
                case "/e":
                    GroupList.displayGroupList();
                    break;

                // show groups which i am a part of
                case "/m":
                    break;

                // group message
                case "/g":
                    break;

                // whisper one peer
                case "/w":
                    if (splitArray.length > 2)
                    {

                        Peer p = PeerList.getPeer(splitArray[1]);


                        if (p != null)
                        {
                            System.out.println( p.toJsonString());
                            ChatMessage m = new ChatMessage();

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
                    Peer p = PeerList.getPeer(splitArray[1]);
                    if (p != null) {
                        Message m = new Message(MessageType.CONTROL);
                        p.sendMessage(m);
                    }
                    break;
                default:
            }
        }

    }


}
