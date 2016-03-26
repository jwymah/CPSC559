/**
 * Shell.java
 *
 * @author Cory Hutchison
 * @author Frankie Yuan
 * @author Jeremy Mah
 */
package com.github.group;

import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

public class Shell extends Thread
{
    private Peer lastPeerMessaged;
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
            //System.out.println("" + input);

            String[] splitArray = input.split(" ",3);
            System.out.println("sa length" + splitArray.length);
            /*for (String a : splitArray)
            {
                System.out.println(a);
            }*/

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
                    if (splitArray.length > 2) {

                        lastPeerMessaged = PeerList.getPeer(splitArray[1]);
                        if (lastPeerMessaged != null) {
                            ChatMessage m = new ChatMessage();
                            m.setDst(lastPeerMessaged.ip, lastPeerMessaged.port);

                            m.setMsgBody(splitArray[2]);
                            m.signMessage();

                            Socket conn = lastPeerMessaged.getConn();
                            try {
                                PrintWriter out = new PrintWriter(conn.getOutputStream(), true);
                                out.println(m.toJsonString());
                                out.flush();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            //lastPeerMessaged.sendMessage(m);
                        }
                    } else {
                        System.out.println("Improper input");
                    }
                    break;
                default:
            }
        }

    }


}
