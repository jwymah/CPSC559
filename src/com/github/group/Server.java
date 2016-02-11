/*
 * The Server file represents the basic barebones implementation
 * of our TCP server.
 *
 * @author  Cory Hutchison
 * @version 0.0
 */ 
package com.github.group;

import java.net.*;
import java.io.*;

import org.apache.commons.cli.*;
//import org.json.simple.*;
//import org.json.simple.parser.*;
//import org.json.simple.parser.ParseException;

public class Server extends Thread {

    protected Socket clientSocket;

    public static void main (String[] argv) throws IOException {
        Boolean running = true;
        ServerSocket serverSocket = null;
        int port = 9999;

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("[+] Server socket created.");
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

    private Server (Socket clientSock) {
        clientSocket = clientSock;
        start();
    }

    public void run() {
        System.out.println("[+] Connection accepted. Thread ID: " + Thread.currentThread().getId());

        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                if (inputLine.equals("quit")) {
                    break;
                }

                System.out.println("[Thread: " + Thread.currentThread().getId() + "] " + inputLine);
                out.println("OK");
            }

            out.close();
            in.close();
            System.out.println("[-] Connection from Thread ID: " + Thread.currentThread().getId() + " has ended.");
            clientSocket.close();
        } catch (IOException e) {
            System.err.println();
            System.exit(1);
        }
    }

}
