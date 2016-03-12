/**
 * Peer.java
 *
 * @author Cory Hutchison
 * @author Frankie Yuan
 * @author Jeremy Mah
 */
package com.github.group;

import java.io.IOException;
import java.net.Socket;

public class Peer {
	private static String CLASS_ID = "PEER.java";
    
    public String   username;
    public String   id;
    public String   ip;
    public int     port;
	private Socket tcpConn;

    // Get instance of Log
    private Log log = Log.getInstance();
    
    /**
     * Constructor
     */
    public Peer(String u, String d, String i, int p) {
        username = u;
        id = d;
        ip = i;
        port = p;
    }

    /**
     * TODO should this throw or handle its own exceptions? the caller has to check a null either way.
     * @param sock
     */
    public void setConnection(Socket sock)
    {
        Socket connectToPeer = null;
		try
		{
			connectToPeer = new Socket(ip, port);
	        String addr = connectToPeer.getInetAddress().getHostAddress() + ":" + connectToPeer.getPort();
	        log.printLogMessage(Log.INFO, CLASS_ID, "Connected: " + addr);
			
	        tcpConn = connectToPeer;
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void clearConnection()
    {
    	tcpConn = null;
    }
    public Socket getConn()
    {
    	return tcpConn;
    }
}
