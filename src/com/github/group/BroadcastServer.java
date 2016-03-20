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

    private static String CLASS_ID = "BroadcastServer";
    private static Log log;

    final static String INET_ADDR = "255.255.255.255";
    final static int PORT = 8888;

    private BroadcastMessage broadcastMessage;

    /**
     * Constructor
     */
    public BroadcastServer() {

        // Get an instance of Log
        log = Log.getInstance();

        // Create BroadcastMessage and convert to Byte array
        // for transmission
        broadcastMessage = new BroadcastMessage();
        byte[] msg = broadcastMessage.toJsonString().getBytes();

        // Attempt to connect to Multicast
        InetAddress addr = null;

        try {
            addr = InetAddress.getByName(INET_ADDR);
        } catch (UnknownHostException e) {
            log.printLogMessage(Log.ERROR, CLASS_ID, "Unable to locate host");
        }

        // Attempt to broadcast
        try (DatagramSocket serverSocket = new DatagramSocket()) {
            DatagramPacket msgPacket = new DatagramPacket(msg, msg.length, addr, PORT);
            serverSocket.send(msgPacket);
        } catch (IOException e) {
            log.printLogMessage(Log.ERROR, CLASS_ID, "Unable to broadcast message");
        }

    }

}
