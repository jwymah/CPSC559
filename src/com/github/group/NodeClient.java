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

    private String ip;
    private int port;

    /**
     * Construction
     */
    public NodeClient(String s, int p) {

        try {
            // Get instance of Log
            log = Log.getInstance();

            // Set IP, Port
            ip = InetAddress.getByName(s).getHostAddress();
            port = p;
        } catch (UnknownHostException e) {
        }

    }

    /**
     * Thread execution code
     */
    public void run() {

        Socket connectToPeer = null;

        try {
            connectToPeer = new Socket(ip, port);
            String addr = connectToPeer.getInetAddress().getHostAddress() + ":" + connectToPeer.getPort();
            log.printLogMessage(Log.INFO, CLASS_ID, "Connected: " + addr);

            PrintWriter out = new PrintWriter(
                    connectToPeer.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                        connectToPeer.getInputStream()));

            out.println("hello.");
            String ok = in.readLine();
            log.printLogMessage(Log.INFO, CLASS_ID, "Received: " + ok);

        } catch (IOException e) {
            log.printLogMessage(Log.ERROR, CLASS_ID, "Unable to connect to peer");
        }

    }

}
