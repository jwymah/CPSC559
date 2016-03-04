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

    public boolean isRunning = false;
    public ServerSocket serverSocket;

    public int port;
    protected Socket clientSocket;

    /**
     * Constructor
     */
    public MessageServer(int port) {
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
        System.out.println("[" + P2PChat.getTimeStamp() +
                "] CONNECTED " + client.getRemoteSocketAddress().toString());

        try {
            PrintWriter out = new PrintWriter(
                    clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                System.out.println("[" + P2PChat.getTimeStamp() +
                        "] " + inputLine);
                if (inputLine.equals("quit")) {
                    break;
                }

                if (inputLine.equals("ping")) {
                    out.println("pong");
                }
            }

            out.close();
            in.close();
            clientSocket.close();
            System.out.println("[" + P2PChat.getTimeStamp() + 
                    "] DISCONNECTED " + client.getRemoteSocketAddress().toString());
        } catch (IOException e) {
            // TODO:    Handle IOException
        }
    }

    /**
     * MessageServer Thread execution 
     */
    public void run() {
        serverSocket = null;

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("[" + P2PChat.getTimeStamp() + 
                    "] Server socket created on Port: " + port);
            try {
                while (isRunning) {
                    clientSocket = serverSocket.accept();
                    clientHandler(clientSocket);
                }
            } catch (IOException e) {
                System.err.println("[!!] Unable to accept connection.");
                System.exit(1);
            }
        } catch (IOException e) {
            // TODO:    Handle IOException
        }
    }
}
