/**
 *  MessageServer.java
 *
 *  @author  Cory Hutchison
 *  @author  Jeremy Mah
 *  @author  Frankie Yuan
 */ 
package com.github.group;

import java.io.*;
import java.net.*;
import java.util.Random;

public class MessageServer extends Thread {

    private static MessageServer instance = null;

    final static int MIN_PORT = 9000;
    final static int MAX_PORT = 10000;

    private static final String CLASS_ID = "MessageServer";
    private static Log log = null;

    private boolean isRunning = false;
    private ServerSocket serverSocket = null;

    private static int SERVER_PORT;
    protected Socket clientSocket;

    /**
     * Constructor
     */
    protected MessageServer() {
        // Get instance of Log
        log = Log.getInstance();

        // Set is running and port
        isRunning = true;
        SERVER_PORT = genPort();
        start();
    }

    /**
     * Returns a single instance of MessageServer
     *
     * @return Instance of MessageServer
     */
    public static MessageServer getInstance() {
        if (instance == null) {
            instance = new MessageServer();
        }
        return instance;
    }


    /**
     * MessageServer Thread execution 
     */
    public void run() {

        // Create a server socket
        try {

            serverSocket = new ServerSocket(SERVER_PORT);
            log.printLogMessage(Log.INFO, CLASS_ID, "");
            printServerInfo();

            try {

                // Accept connections
                while (isRunning) {
                    clientSocket = serverSocket.accept();

                    // Hand off to client handler thread
                    new ClientHandler(clientSocket).start();
                }
            } catch (IOException e) {
                log.printLogMessage(Log.ERROR, CLASS_ID, "Unable to accept connection");
            }
        } catch (IOException e) {
            log.printLogMessage(Log.ERROR, CLASS_ID, "Unable to create socket");
        }
    }

    /**
     * Shuts down the MessageServer
     */
    public void shutdown() {
        isRunning = false;
    }

    /**
     * Prints server IP:Port
     */
    private void printServerInfo() {
        System.out.println();
        System.out.println("\tIP:\t\t127.0.0.1");
        System.out.println("\tPort:\t\t" + getPort());
        System.out.println();
    }

    /**
     * Generates a random integer between MIN_PORT and MAX_PORT.
     *
     * @return A random integer between MIN_PORT and MAX_PORT
     */
    private static int genPort() {

        log.printLogMessage(Log.INFO, CLASS_ID, "Generating Port");

        Random rand = new Random();
        int i = rand.nextInt((MAX_PORT - MIN_PORT) + 1) + MIN_PORT;

        return i;
    }

    /**
     * Get the current port that MessageServer is listening on
     */
    public static int getPort() {
        return SERVER_PORT;
    }

    private class ClientHandler extends Thread {
        private static final String CLASS_ID = "ClientHandler";
        private Socket client = null;

        /**
         * Constructor
         */
        public ClientHandler(Socket c) {
            client = c;
        }

        /**
         * Client Handler
         *
         * @param   client  The client socket
         */
        public void run() {
            String addr = client.getInetAddress().getHostAddress() + ":" + client.getPort();

            log.printLogMessage(Log.INFO, CLASS_ID, 
                    "Connected:\t" + addr);

            try {

                // Get reader/writer
                PrintWriter out = new PrintWriter(
                        clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(
                            clientSocket.getInputStream()));

                String inputLine;

                // Read input from client
                while ((inputLine = in.readLine()) != null) {
                    // Handle `quit` message
                    if (inputLine.equals("/quit")) {
                        break;
                    }

                    // Handle `ping` message
                    if (inputLine.equals("/ping")) {

                        out.println("/pong");

                    }
                    log.printLogMessage(Log.MESSAGE, CLASS_ID, addr + ": " + inputLine);
//                    log.printLogMessage(Log.MESSAGE, CLASS_ID, "local address: " + InetAddress.getLocalHost().getHostAddress());
                }

                log.printLogMessage(Log.INFO, CLASS_ID, 
                        "Disconnected:\t" + addr);

                // Clean up connections
                out.close();
                in.close();
                clientSocket.close();

            } catch (IOException e) {
                log.printLogMessage(Log.ERROR, CLASS_ID, 
                        "Connection interrupted");
            }
        }
    }
}
