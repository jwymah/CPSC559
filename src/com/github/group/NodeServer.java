/**
 *  NodeServer.java
 *
 *  @author  Cory Hutchison
 *  @author  Jeremy Mah
 *  @author  Frankie Yuan
 */ 
package com.github.group;

import java.io.*;
import java.lang.Process;
import java.net.*;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Random;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class NodeServer extends Thread {

    private static NodeServer instance = null;

    final static int MIN_PORT = 9000;
    final static int MAX_PORT = 10000;

    private static final String CLASS_ID = "NodeServer";
    private static Log log = null;
    private static int SERVER_PORT;
    private static String SERVER_IP;

    private boolean isRunning = false;
    private ServerSocket serverSocket = null;

    protected Socket clientSocket;

    /**
     * Constructor
     */
    protected NodeServer() {

        //ipLookup();

        try {
            // Get instance of Log
            log = Log.getInstance();

            // Set is running and port
            isRunning = true;

            String ip;
            if (System.getProperty("os.name").equals("Mac OS X")) {
                NetworkInterface iface = NetworkInterface.getByName("en0");
                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                InetAddress addr = addresses.nextElement();
                ip = addr.getHostAddress();
            } else {
                ip = InetAddress.getLocalHost().getHostAddress();
            }

            SERVER_IP = ip;
            SERVER_PORT = genPort();
        } catch (UnknownHostException e) {
        } catch (SocketException e) {
        }

        start();

    }

    /**
     * Returns a single instance of NodeServer
     *
     * @return Instance of NodeServer
     */
    public static NodeServer getInstance() {

        if (instance == null) {
            instance = new NodeServer();
        }

        return instance;
    }


    /**
     * NodeServer Thread execution 
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
                    new MessageHandler(clientSocket).start();

                }

            } catch (IOException e) {

                log.printLogMessage(Log.ERROR, CLASS_ID, "Unable to accept connection");

            }

        } catch (IOException e) {

            log.printLogMessage(Log.ERROR, CLASS_ID, "Unable to create socket");

        }

    }

    /**
     * Shuts down the NodeServer
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

    public static void ipLookup() {
        String ip;
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                //filters out 127.0.0.1 and inactive interfaces
                if (iface.isLoopback() || !iface.isUp()) continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while(addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    ip = addr.getHostAddress();
                    System.out.println(iface.getDisplayName() + "\t" + ip);
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     */
    public static String getIP() {
        return SERVER_IP;
    }

    /**
     * Get the current port that NodeServer is listening on
     */
    public static int getPort() {

        return SERVER_PORT;

    }

    private class MessageHandler extends Thread {

        private static final String CLASS_ID = "MessageHandler";
        private Socket client = null;

        /**
         * Constructor
         */
        public MessageHandler(Socket c) {

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
                    "Connected: " + addr);

            try {

                // Get reader/writer
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                            clientSocket.getOutputStream()));
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

                        out.write("/pong\n");

                    }

                    // Handle `help` or `?` message
                    if (inputLine.equals("/?") || inputLine.equals("/help")) {
                        out.write("/ping - ping\n");
                        out.write("/quit - disconnect\n");
                    }

                    // Return OK message so that client knows message is
                    // received
                    out.write("OK\n");
                    out.flush();
                    // Log message to stdout
                    log.printLogMessage(Log.MESSAGE, CLASS_ID, addr + ": " + inputLine);

                }

                log.printLogMessage(Log.INFO, CLASS_ID, 
                        "Disconnected: " + addr);

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
