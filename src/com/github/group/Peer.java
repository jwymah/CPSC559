/**
 * Peer.java
 *
 * @author Cory Hutchison
 * @author Frankie Yuan
 * @author Jeremy Mah
 */
package com.github.group;

public class Peer {
    
    public String   username;
    public String   id;
    public String   ip;
    public int     port;

    /**
     * Constructor
     */
    public Peer(String u, String d, String i, int p) {
        username = u;
        id = d;
        ip = i;
        port = p;
    }

}
