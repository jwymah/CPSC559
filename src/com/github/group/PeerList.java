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
	private static String CLASS_ID = "PeerList";
	
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
    
    /**
     *
     */
    public synchronized static void addPeer(Peer peerToAdd) {
    	if (peersByName.get(peerToAdd.username) != null)
    	{
            log.printLogMessage(Log.ERROR, CLASS_ID, 
                    "Unable to add user to PeerList - a user by this name already exists");
            return;
    	}
    	peersByName.put(peerToAdd.username, peerToAdd);
    	peersByIP.put(peerToAdd.getInetString(), peerToAdd);
    	//displayPeerList();
        log.printLogMessage(Log.INFO, CLASS_ID, "Added: " + peerToAdd.username);
    }
    
    /**
     * Retrieve the peer with an associated username
     *
     * @return the Peer with the associated username, null if there is no such peer
     */
    public synchronized static Peer getPeer(String username) {
        if (!peersByName.containsKey(username)){
            return null;
        }

    	return peersByName.get(username);
    }
    
    /**
     *
     */
    public synchronized static Peer getPeer(String ip, int port) {
    	return peersByIP.get(ip + ":" + String.valueOf(port));
    }
    
    /**
     *
     */
    public synchronized static Collection<Peer> getAllPeers() {
    	//TODO: this should return a copy of the collection. currently it is backed by the collection and can cause iteration/concurrency problems
    	return peersByName.values();
    }

    /**
     *
     */
    public synchronized static void displayPeerList() {
    	int i = 0;
    	for (Peer p : peersByName.values())
    	{
    		System.out.println(i + ": " + p.toJsonString());
    		i++;
    	}
    }

    /**
     *
     */
    public static void displayPeerListUsernames() {
        for (String p : peersByName.keySet())
        {
            System.out.println(p);
        }
    }




    /**
     * Removes a disconnected/error Peer
     */
    public synchronized static void removePeer(Peer p) {
        log.printLogMessage(Log.INFO, CLASS_ID, "Removed: " + p.username);
        peersByName.remove(p.username);
        peersByIP.remove(p.getInetString());
    }

	public static Peer getAPeer()
	{
		if (peersByIP.size() > 0)
		{
			return (Peer) peersByIP.values().toArray()[0];
		}
		return null;
	}

}
