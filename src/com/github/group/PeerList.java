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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PeerList {
	private static Log log = Log.getInstance();
	private static String CLASS_ID = "PeerList";
	
    private static Map<String, Peer> peersByName;
    private static Map<String, Peer> peersId;
    private static PeerList instance = null;

    /**
     * Constructor
     */
    protected PeerList() {
        // The purpose of having two maps is to search by by name or IP+port
        peersByName = new HashMap<String, Peer>();
        peersId = new HashMap<String, Peer>();
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
     * @param peerToAdd The peer to be added to the peer list
     */
    public synchronized static void addPeer(Peer peerToAdd) {
    	if (peersByName.get(peerToAdd.username) != null)
    	{
            log.printLogMessage(Log.ERROR, CLASS_ID, 
                    "Unable to add user to PeerList - a user by this name already exists");
            return;
    	}
    	peersByName.put(peerToAdd.username, peerToAdd);
    	peersId.put(peerToAdd.id, peerToAdd);
    	//
        log.printLogMessage(Log.INFO, CLASS_ID, "Added: " + peerToAdd.username);
    }
    
    /**
     * Retrieve the peer with an associated username. Only returns 
     * the first instance of a user with specified name.
     *
     * @param username The username
     * @return the Peer with the associated username, null if there is no such peer
     */
    public synchronized static Peer getPeerByName(String username) {
        if (!peersByName.containsKey(username))
        {
            return null;
        }

    	return peersByName.get(username);
    }
    
    /**
     * @param id The user id
     * @return the peer with the associated id
     */
    public synchronized static Peer getPeerById(String id)
    {
    	return peersId.get(id);
    }
    
    /**
     * @return a copy of the peerlist
     */
    public synchronized static Collection<Peer> getAllPeers() {
    	Set<Peer> copy = new HashSet<Peer>();
    	copy.addAll(peersByName.values());
    	return copy;
    }

    /**
     *
     */
    public synchronized static void displayPeerList() {
        log.printLogMessage(Log.INFO, CLASS_ID, "Displaying PeerList");
    	System.out.println("\nNo. of peers: " + peersByName.size());
    	for (Peer p : peersByName.values())
    	{
            System.out.println("\tUsername:\t" + p.username);
            System.out.println("\tID:\t\t" + p.id);
            System.out.println("\tAddress:\t" + p.ip + ":" + p.port);
            System.out.println();
    	}
    }

    /**
     *
     */
    public static void displayPeerListUsernames() {
        for (String p : peersByName.keySet())
        {
            System.out.println(p + " ID: " + peersByName.get(p).id);
        }
    }

    /**
     * Removes a disconnected/error Peer
     * @param p The peer to be removed
     */
    public synchronized static void removePeer(Peer p) {
        log.printLogMessage(Log.INFO, CLASS_ID, "Removed: " + p.username);
        peersByName.remove(p.username);
        peersId.remove(p.id);
    }

    /**
     * Just returns any peer in the peerlist
     *
     * @return A peer in the peerlist or null if the list is empty
     */
	public static Peer getAPeer()
	{
		if (peersId.size() > 0)
		{
			return (Peer) peersId.values().toArray()[0];
		}
		return null;
	}

    public synchronized static void messageAllPeers(Message msg)
    {
        for (Peer p: getAllPeers())
        {
            p.sendMessage(msg);
        }
    }
}
