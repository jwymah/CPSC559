/**
 *  BroadcastClient.java
 *
 *  @author Cory Hutchison
 *  @author Frankie Yuan
 *  @author Jeremy Mah
 */
package com.github.group;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class BroadcastClient extends Thread {

    private final static String CLASS_ID = "BroadcastClient";
    private static Log log;
    
    final static String INET_ADDR = "224.0.0.3";
    final static int PORT = 8888;

    /**
     * Constructor
     */
    public BroadcastClient() {

        log = Log.getInstance();

    }

    public void run() {

        // Initialization
        InetAddress address = null;
        SocketAddress sockAddress = null;
        NetworkInterface netInterface = null;

        // Lookup address and attempt to connect
        try {

            address = InetAddress.getByName(INET_ADDR);
            sockAddress = new InetSocketAddress(address, PORT);
            netInterface = NetworkInterface.getByName("en0");

        } catch (UnknownHostException e) {

            log.printLogMessage(Log.ERROR, CLASS_ID, "Unable to locate host");

        } catch (SocketException e) {

            log.printLogMessage(Log.ERROR, CLASS_ID, "Unable to create socket");

        }

        // Create buffer
        byte[] buf = new byte[512];

        // Attempt to join group and get messages
        try (MulticastSocket clientSocket = new MulticastSocket(PORT)) {

            clientSocket.joinGroup(sockAddress, netInterface);

            while (true) {
                DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
                clientSocket.receive(msgPacket);

                String msg = new String(buf, 0, buf.length);
                BroadcastMessage bMsg = new BroadcastMessage(msg);
                bMsg.printMessage();
                Peer newPeer = new Peer(bMsg.username, bMsg.id, bMsg.ip, bMsg.port);
                PeerList.addPeer(newPeer);
                (new NodeClient(bMsg.ip, (int) bMsg.port, newPeer)).start();
            }

        } catch (IOException e) {
            log.printLogMessage(Log.ERROR, CLASS_ID, "Unable to join group");
        }

    }

}
