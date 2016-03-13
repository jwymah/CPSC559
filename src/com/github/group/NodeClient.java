/**
 *  NodeClient.java
 *
 *  @author Cory Hutchison
 *  @author Frankie Yuan
 *  @author Jeremy Mah
 */
package com.github.group;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class NodeClient extends Thread {

    private final String CLASS_ID = "NodeClient";
    private static Log log;

    private Peer peer;

    /**
     * Construction
     */
    public NodeClient(String s, int p, Peer peer) {
        // Get instance of Log
        log = Log.getInstance();
        this.peer = peer;
    }

    /**
     * Thread execution code
     */
    public void run() {

        Socket conn = peer.getConn();

        try {
            PrintWriter out = new PrintWriter(
                    conn.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));

            // Send own contact info over the socket on opening
            BroadcastMessage broadcastMessage = new BroadcastMessage();
            String msg = broadcastMessage.toString();
            out.println(msg);
//            String ok = in.readLine();
//            log.printLogMessage(Log.INFO, CLASS_ID, "Received: " + ok);
            
            GroupList.getInstance().mockMessageGroup("sending message to group members [in response to receiving broadcast]");
            System.out.println("+++++++++++++++++");
            PeerList.displayPeerList();// Read input from client
            
            String inputLine;
            while ((inputLine = in.readLine()) != null) {

                // Handle `quit` message
                if (inputLine.equals("/quit")) {
                    break;
                }

                // Handle `ping` message
                if (inputLine.equals("/ping")) {
                    out.write("/pong\n");
                }

                // Handle `help` or `?` message
                if (inputLine.equals("/?") || inputLine.equals("/help")) {
                    out.write("/ping - ping\n");
                    out.write("/quit - disconnect\n");
                }

                // Return OK message so that client knows message is
                // received
//                out.write("OK\n");
//                out.flush();
                // Log message to stdout
                String addr = conn.getInetAddress().getHostAddress() + ":" + conn.getPort();
                log.printLogMessage(Log.MESSAGE, CLASS_ID, addr + ": " + inputLine);
            }

        } catch (IOException e) {
            log.printLogMessage(Log.ERROR, CLASS_ID, "Trouble reading/writing to peer.");
        }

    }
}
