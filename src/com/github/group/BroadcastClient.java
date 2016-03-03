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
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class BroadcastClient extends Thread {
    
    final static String INET_ADDR = "224.0.0.3";
    final static int PORT = 8888;

    /**
     * Constructor
     */
    public BroadcastClient() {
    }

    public void run() {
        InetAddress address = null;
        SocketAddress sockAddress = null;
        NetworkInterface netInterface = null;

        try {
            address = InetAddress.getByName(INET_ADDR);
            sockAddress = new InetSocketAddress(address, PORT);
            netInterface = NetworkInterface.getByName("en0");
        } catch (UnknownHostException e) {
            // TODO:    Handle UnknownHostException
        } catch (SocketException e) {
            // TODO:    Handle SocketException
        }

        byte[] buf = new byte[512];

        try (MulticastSocket clientSocket = new MulticastSocket(PORT)) {
            clientSocket.joinGroup(sockAddress, netInterface);

            while (true) {
                DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
                clientSocket.receive(msgPacket);

                String msg = new String(buf, 0, buf.length);

                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

                System.out.println("[" + sdf.format(cal.getTime()) + "] " + msg);
            }
        } catch (IOException e) {
            // TODO:    Handle IOException
        }

    }

}
