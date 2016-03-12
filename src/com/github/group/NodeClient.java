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
            
            String ok = in.readLine();
            log.printLogMessage(Log.INFO, CLASS_ID, "Received: " + ok);

        } catch (IOException e) {
            log.printLogMessage(Log.ERROR, CLASS_ID, "Trouble reading/writing to peer.");
        }

    }
}
