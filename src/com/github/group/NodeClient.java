/**
 *  NodeClient.java
 *
 *  @author Cory Hutchison
 *  @author Frankie Yuan
 *  @author Jeremy Mah
 */
package com.github.group;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;

public class NodeClient extends Thread {

    private static Log log;
    private final String CLASS_ID = "NodeClient";
    private Peer peer;

    /**
     * Construction
     */
    public NodeClient(String s, int p, Peer peer) {
        // Get instance of Log
        log = Log.getInstance();
        this.peer = peer;
    }

    /**
     * Thread execution code
     */
    public void run() {
        try {
            Socket conn = peer.getConn();
            PrintWriter out = new PrintWriter(conn.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            // Send own contact info over the socket on opening
            BroadcastMessage broadcastMessage = new BroadcastMessage();
            String msg = broadcastMessage.toString();
            out.println(msg);

            // Read input from client
            String inputLine;
        	
        	//TODO: refactor this into common library for nodeserver+nodeclient
            while ((inputLine = in.readLine()) != null) {
            	switch (Message.parseMessageType(inputLine)){
            		case BROADCAST:
            			parseAndStoreConnectingPeer(inputLine, conn);
                        GroupList.getInstance().mockMessageGroup("sending message to group members [in response to receiving info] [2]");
            			break;
            		case CHAT:
            			ChatMessage cmsg = new ChatMessage(inputLine);
            			out.println(new Message(MessageType.CONTROL).toJsonString());
            			break;
					case CONTROL:
						break;
					case QUERY:
						break;
					case QUERY_RESPONSE:
						break;
					case BLANK:
					default:
						System.out.println("received BLANK_TYPE message type?");
						break;
            	}
                // Log message to stdout
                String addr = conn.getInetAddress().getHostAddress() + ":" + conn.getPort();
                log.printLogMessage(Log.MESSAGE, CLASS_ID, addr + ": " + inputLine);
            }

        } catch (IOException e) {
            log.printLogMessage(Log.ERROR, CLASS_ID, "Trouble reading/writing to peer.");
        }
    }

	//TODO: refactor this into common library for nodeserver+nodeclient
    public void parseAndStoreConnectingPeer(String inputLine, Socket sock)
	{
		// The first thing received on this socket is the contact info of the connecting peer
		BroadcastMessage bMsg = new BroadcastMessage(inputLine);
		
		Peer newPeer = new Peer(bMsg.username, bMsg.id, bMsg.ip, bMsg.port, sock);
		PeerList.getInstance().addPeer(newPeer);
		bMsg.printMessage();
	}
}
