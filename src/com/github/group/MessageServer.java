/**
 *  MessageServer.java
 *
 *  @author  Cory Hutchison
 *  @author  Jeremy Mah
 *  @author  Frankie Yuan
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

public class MessageServer extends Thread {

    private static final String CLASS_ID = "MessageServer";
    private static Log log;

    public boolean isRunning = false;
    public ServerSocket serverSocket;

    public int port;
    protected Socket clientSocket;

    /**
     * Constructor
     */
    public MessageServer(int port) {

        // Get instance of Log
        log = Log.getInstance();

        // Set is running and port
        isRunning = true;
        this.port = port;

    }

    /**
     * Client Handler
     *
     * @param   client  The client socket
     */
    public void clientHandler(Socket client) {
        // TODO:    Fix this so that it actually shows the IP:PORT of the
        //          incoming connection.
        log.printLogMessage(Log.INFO, CLASS_ID, 
                "CONNECTED" + client.getRemoteSocketAddress().toString());

        try {
            // Get reader/writer
            PrintWriter out = new PrintWriter(
                    clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;

            // Read input from client
            while ((inputLine = in.readLine()) != null) {
                log.printLogMessage(Log.INFO, CLASS_ID, inputLine);

                // Handle `quit` message
                if (inputLine.equals("/quit")) {
                    break;
                }

                // Handle `ping` message
                if (inputLine.equals("/ping")) {
                    out.println("/pong");
                }
            }

            log.printLogMessage(Log.INFO, CLASS_ID, "DISCONNECTED" + 
                    client.getRemoteSocketAddress().toString());

            // Clean up connections
            out.close();
            in.close();
            clientSocket.close();

        } catch (IOException e) {
            log.printLogMessage(Log.ERROR, CLASS_ID, 
                    "Unable to create read/write connection");
        }

    }

    /**
     * MessageServer Thread execution 
     */
    public void run() {
        serverSocket = null;

        // Create a server socket
        try {
            serverSocket = new ServerSocket(port);
            log.printLogMessage(Log.INFO, CLASS_ID, "Running: " + getServerInfo());
            try {
                // Accept connections
                while (isRunning) {
                    clientSocket = serverSocket.accept();
                    clientHandler(clientSocket);
                }
            } catch (IOException e) {
                log.printLogMessage(Log.ERROR, CLASS_ID, "Unable to accept connection");
                System.exit(1);
            }
        } catch (IOException e) {
            log.printLogMessage(Log.ERROR, CLASS_ID, "Unable to create socket");
        }
    }

    /**
     * Returns server IP:Port
     */
    private String getServerInfo() {
        return "127.0.0.1:" + port;
    }
}
