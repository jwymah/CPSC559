/**
 *  BroadcastServer.java
 *
 *  @author Cory Hutchison
 *  @author Frankie Yuan
 *  @author Jeremy Mah
 */
package com.github.group;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class BroadcastServer extends Thread {

    final static String INET_ADDR = "224.0.0.3";
    final static int PORT = 8888;

    private BroadcastMessage broadcastMessage;

    /**
     * Constructor
     */
    public BroadcastServer() {

        broadcastMessage = new BroadcastMessage();
        byte[] msg = broadcastMessage.toString().getBytes();

        InetAddress addr = null;

        try {
            addr = InetAddress.getByName(INET_ADDR);
        } catch (UnknownHostException e) {
            // TODO:    Handle UnknownHostException
        }

        try (DatagramSocket serverSocket = new DatagramSocket()) {
            DatagramPacket msgPacket = new DatagramPacket(msg, msg.length, addr, PORT);
            serverSocket.send(msgPacket);
        } catch (IOException e) {
            // TODO:    Handle IOException
        }

    }

}
