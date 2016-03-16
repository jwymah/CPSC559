/**
 * PeerList.java
 *
 * @author Cory Hutchison
 * @author Frankie Yuan
 * @author Jeremy Mah
 */
package com.github.group;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PeerList {
	private static Log log = Log.getInstance();
	private static String CLASS_ID = "PeerList.java";
	
    private static Map<String, Peer> peersByName;
    private static Map<String, Peer> peersByIP;
    private static PeerList instance = null;

    /**
     * Constructor
     */
    protected PeerList() {
        // The purpose of having two maps is to search by by name or IP+port
        peersByName = new HashMap<String, Peer>();
        peersByIP = new HashMap<String, Peer>();
    }

    /**
     * Returns an instance of PeerList
     * 
     * @return An instance of the peerlist
     */
    public static PeerList getInstance() {
        if (instance == null) {
            instance = new PeerList();
        }
        return instance;
    }
    
    public static void addPeer(Peer peerToAdd)
    {
    	if (peersByName.get(peerToAdd.username) != null)
    	{
            log.printLogMessage(Log.ERROR, CLASS_ID, "Unable to add user to PeerList - a user by this name already exists");
            return;
    	}
    	peersByName.put(peerToAdd.username, peerToAdd);
    	peersByIP.put(peerToAdd.getInetString(), peerToAdd);
    	displayPeerList();
    }
    
    public static Peer getPeer(String username)
    {
    	return peersByName.get(username);
    }
    
    public static Peer getPeer(String ip, int port)
    {
    	return peersByIP.get(ip + ":" + String.valueOf(port));
    }
    
    public static Collection<Peer> getAllPeers()
    {
    	return peersByName.values();
    }
    
    public static void displayPeerList()
    {
    	int i = 0;
    	for (Peer p : peersByName.values())
    	{
    		System.out.println(i + ": " + p.toJsonString());
    		i++;
    	}
    }

}
