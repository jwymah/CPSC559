/**
 *  MessageClient.java
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

public class MessageClient extends Thread {

    private final String CLASS_ID = "MessageClient";
    private static Log log;

    private String ip;
    private int port;

    /**
     * Construction
     */
    public MessageClient(String s, int p) {

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

            out.println("/ping");
            String pong = in.readLine();
            String ok = in.readLine();
            log.printLogMessage(Log.INFO, CLASS_ID, "Received: " + pong + " " + ok);

        } catch (IOException e) {
            log.printLogMessage(Log.ERROR, CLASS_ID, "Unable to connect to peer");
        }

    }

}
