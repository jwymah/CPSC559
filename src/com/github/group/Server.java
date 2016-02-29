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

    protected Socket clientSocket;
    static int port = 9999;

    private static String id;

    public static void main (String[] argv) throws IOException {
        Boolean running = true;
        ServerSocket serverSocket = null;

        // Set server id
        // Cory: Eventually we will need to store this information in a config
        // file or something so that we aren't changing IDs on every restart.
        if (id == null) {
            createID();
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
            System.out.println(msg.getMessage());
        */

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("[+] Server socket created on port "+port+".");
            try {
                while (running) {
                    new Server(serverSocket.accept());
                }
            } catch (IOException e) {
                System.err.println("[!!] Unable to accept connection.");
                System.exit(1);
            }
        } catch (IOException e) {
            System.err.println("[!!] Unable to listen on port "+port+".");
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
            JSONParser parser = new JSONParser();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                try {
                    Object obj = parser.parse(inputLine);
                    JSONObject jsonObj = (JSONObject) obj;
                    String msgType = (String) jsonObj.get("MessageType");

                    // Quit for testing with netcat
                    if (msgType.equals("bye")) {
                        break;
                    }

                    // Ping
                    if (msgType.equals("ping")) {
                        Message msg = new Message(
                                getID(),
                                "nil",
                                "pong",
                                "pong");
                        out.println(msg.getMessage());
                    }

                    // Test Message
                    if (msgType.equals("test")) {
                        Message msg = new Message(
                                getID(), 
                                "nil", 
                                "TEST", 
                                "This is a test message.");
                        out.println(msg.getMessage());
                    }

                    System.out.println("[Thread: " + Thread.currentThread().getId() 
                                       + "] " + inputLine);
                } catch (ParseException e) {
                    System.out.println("[!!] [Thread: " + Thread.currentThread().getId()
                            + "] Received invalid input: " + inputLine);
                    out.println("[!!] Received invalid input: " + inputLine);
                }
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

    public static String getID() {
        return id;
    }

}
