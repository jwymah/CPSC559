/**
 *  NodeClient.java
 *
 *  @author Cory Hutchison
 *  @author Frankie Yuan
 *  @author Jeremy Mah
 */
package com.github.group;

import controlMessages.ControlMessage;

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
        // Send own contact info over the socket on opening
		BroadcastMessage broadcastMessage = new BroadcastMessage();
		peer.sendMessage(broadcastMessage);
		
		// Read input from client
		String inputLine;

		try
		{
			//TODO: refactor this into common library for nodeserver+nodeclient. after user input is added
			while ((inputLine = peer.getNextLine()) != null) {
				switch (Message.parseMessageType(inputLine)){
					case BROADCAST:
						//ignore
						break;
					case CHAT:
						ChatMessage cmsg = new ChatMessage(inputLine);
	                    cmsg.printMessage();
						cmsg.toJsonString();
						break;
					case CONTROL:
						ControlMessage controlMsg = new ControlMessage(inputLine);
						GroupManager.handleControlMessage(peer,controlMsg);
						break;
					case BLANK:
					default:
	                    log.printLogMessage(Log.INFO, CLASS_ID, "Received BLANK_TYPE message");
						//System.out.println("received BLANK_TYPE message type?");
						break;
				}
			    // Log message to stdout
			    String addr = peer.getConn().getInetAddress().getHostAddress() + ":" + peer.getConn().getPort();
			    //log.printLogMessage(Log.MESSAGE, CLASS_ID, addr + ": " + inputLine);
			}
		} finally {
	        // Log that they have disconnected
	        log.printLogMessage(Log.INFO, CLASS_ID, "Disconnected: " + peer.username);
	
	        // Remove from all groups
	        GroupList.getInstance().removePeerFromAllGroups(peer);
	        
	        // Remove from PeerList
	        PeerList.removePeer(peer);
	
	        // Clean up connections
	        peer.clearConnection();
		}
    }
}
