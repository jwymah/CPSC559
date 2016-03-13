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

import org.json.simple.JSONObject;

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
     * Use this constructor when a TCP connection is not yet established
     */
    public Peer(String u, String d, String i, int p) {
        username = u;
        id = d;
        ip = i;
        port = p;
        connect();
    }
    /**
     * use this constructor if a connection is already established
     */
    public Peer(String u, String d, String i, int p, Socket sock) {
        username = u;
        id = d;
        ip = i;
        port = p;
        tcpConn = sock;
    }

    /**
     * TODO should this throw or handle its own exceptions? the caller has to check a null either way.
     * @param sock
     */
    public void connect()
    {
		try
		{
			Socket connectToPeer = new Socket(ip, port);
	        String addr = connectToPeer.getInetAddress().getHostAddress() + ":" + connectToPeer.getPort();
	        log.printLogMessage(Log.INFO, CLASS_ID, "Connected: " + addr);
			
	        tcpConn = connectToPeer;
		}
		catch (IOException e)
		{
            log.printLogMessage(Log.ERROR, CLASS_ID, "Unable to connect to peer");
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

	public String getInetString()
	{
		return ip + ":" + String.valueOf(port);
	}
	
	@Override
	public String toString()
	{
        JSONObject msg = new JSONObject();
        msg.put("Username", username);
        msg.put("ID", id);
        msg.put("IP", ip);
        msg.put("Port", port);
        msg.put("TcpConn", tcpConn.getInetAddress());
        
        return msg.toString();
	}
}
