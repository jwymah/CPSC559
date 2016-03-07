/**
 * PeerList.java
 *
 * @author Cory Hutchison
 * @author Frankie Yuan
 * @author Jeremy Mah
 */
package com.github.group;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class PeerList {

    public static ArrayList<Peer> list;
    private static PeerList instance = null;

    /**
     * Constructor
     */
    protected PeerList() {

        list = new ArrayList();

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

}
