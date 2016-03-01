/**
 * File:    Server.java
 *
 * The Server file represents the basic implementation
 * of a TCP server, it passes JSON messages.
 *
 * @author  Cory Hutchison
 * @author  Jeremy Mah
 * @author  Frankie Yuan
 * @version 0.0
 */ 
package com.github.group;

import java.net.*;
import java.io.*;
import java.util.Date;
import java.util.Random;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.lang.Process;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONObject;

// Cory: Ignoring for now, we can add interface after basic functionality is
// completed.
// import org.apache.commons.cli.*;

public class Server extends Thread {

    private static final int minPort = 9000;
    private static final int maxPort = 9005;

    protected Socket clientSocket;

    private static String id;
    private static int port;

    public static void main (String[] argv) {
        Boolean running = true;
        ServerSocket serverSocket = null;

        // Set server id
        // Cory: Eventually we will need to store this information in a config
        // file or something so that we aren't changing IDs on every restart.
        if (id == null) {
            try {
                createID();
            } catch (IOException e) {
                System.out.println("[!!] Failed to create a HostID.");
            }
        }

        // Cory: Testing message 
        /*
            Message msg = new Message(getID(), "nil", "TEST", "This is a test message.");
            System.out.println(msg.getStamp());
            System.out.println(msg.getHostID());
            System.out.println(msg.getGroupID());
            System.out.println(msg.getMessageID());
            System.out.println(msg.getMessageType());
            System.out.println(msg.getMessageBody());
            System.out.println(msg.getJSONMessage());
        */

        port = generatePort();

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("[+] Server socket created on port " + 
                    port + ".");
            broadcast();
            try {
                while (running) {
                    new Server(serverSocket.accept());
                }
            } catch (IOException e) {
                System.err.println("[!!] Unable to accept connection.");
                System.exit(1);
            }
        } catch (IOException e) {
            System.err.println(
                    "[!!] Unable to listen on port " + port + ".");
            System.exit(1);
        }

        Socket clientSocket = null;
        System.out.println("[+] Waiting for connection.");

        try {
            while (true) {
                clientSocket = serverSocket.accept();
            }
        } catch (IOException e) {
            System.err.println("[!!] Unable to create connection to client.");
            System.exit(1);
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.err.println("[!!] Unable to close port "+port+".");
                System.exit(1);
            }
        }
    }

    /**
     * Server creates a new instance of a server, and starts the new thread for
     * the client.
     *
     * @param   clientSock  Incoming client socket.
     */
    private Server (Socket clientSock) {
        clientSocket = clientSock;
        start();
    }

    /**
     * Thread execution code.
     */
    public void run() {
        System.out.println("[+] Connection accepted. Thread ID: " 
                           + Thread.currentThread().getId());

        try {
            PrintWriter out = new PrintWriter(
                    clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                Message inputMsg = new Message(inputLine);

                // Quit for testing with netcat
                if (inputMsg.getMessageType().equals("bye")) {
                    break;
                }

                // Respond to broadcast messages
                if (inputMsg.getMessageType().equals("broadcast")) {
                    // Respond to broadcast
                    Message msg = new Message(
                            getID(),
                            "nil",
                            "response",
                            "localhost:"+port);

                    out.println(msg.getJSONMessage());
                }

                // Ping
                if (inputMsg.getMessageType().equals("ping")) {
                    Message msg = new Message(
                            getID(),
                            "nil",
                            "response",
                            "pong");
                    out.println(msg.getJSONMessage());
                }

                // Response Message
                if (inputMsg.getMessageType().equals("response")) {
                    System.out.println(inputMsg.getMessageBody());
                }

                // Test Message
                if (inputMsg.getMessageType().equals("test")) {
                    Message msg = new Message(
                            getID(), 
                            "nil", 
                            "response", 
                            "This is a test message.");
                    out.println(msg.getJSONMessage());
                }

                System.out.println("[Thread: " + 
                        Thread.currentThread().getId() + "] " 
                        + inputLine);
            }

            // Cleanup
            System.out.println("[-] Connection from Thread ID: " 
                               + Thread.currentThread().getId() + " has ended.");
            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            System.err.println();
            System.exit(1);
        }
    }

    /**
     *
     */
    private static void broadcast() {
        System.out.println("[+] Broadcasting to local network.");
        // Message to broadcast
        Message outMsg = new Message(
                getID(),
                "nil",
                "broadcast",
                "localhost:" + port);

        for (int i = minPort; i <= maxPort; i++) {
            // Don't message yourself
            if (i != port) {
                try {
                    // Create connection
                    Socket s = new Socket("localhost", i);

                    System.out.println("[+] Connection accepted. Thread ID: " 
                                       + Thread.currentThread().getId());

                    // Set up reader/writer
                    PrintWriter out = new PrintWriter(s.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(s.getInputStream()));

                    // Broadcast to peer
                    out.println(outMsg.getJSONMessage());

                    // Print response
                    String incoming;
                    if ((incoming = in.readLine()) != null) {
                        Message inMsg = new Message(incoming);
                        System.out.println(inMsg.getJSONMessage());
                    }

                    s.close();
                    in.close();
                    out.close();
                    System.out.println("[-] Connection from Thread ID: " 
                                       + Thread.currentThread().getId() + " has ended.");
                } catch (IOException e) {
                    System.out.println("[!] Unable to connect to localhost:" + i);
                }
            }
        }
    }

    /**
     *
     */
    private static void createID() throws IOException {
        Runtime rt = Runtime.getRuntime();
        String[] commands = {"uuidgen"};
        Process proc = rt.exec(commands);

        BufferedReader stdIn = new BufferedReader(
                new InputStreamReader(proc.getInputStream()));

        BufferedReader stdErr = new BufferedReader(
                new InputStreamReader(proc.getErrorStream()));

        String uuid = stdIn.readLine();

        id = uuid;
    }

    /**
     * Allows other classes to access this server's ID.
     *
     * @return  This servers ID
     */
    public static String getID() {
        return id;
    }

    /**
     * Generates a random integer between 9000-9025.
     *
     * @return A random integer between 9000-9025
     */
    private static int generatePort() {
        Random rand = new Random(); 

        return rand.nextInt((maxPort - minPort) + 1) + minPort;
    }

}
