/**
 * Peer.java
 *
 * @author Cory Hutchison
 * @author Frankie Yuan
 * @author Jeremy Mah
 */
package com.github.group;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.simple.JSONObject;

public class Peer {
	private static String CLASS_ID = "PEER.java";
    
    public String   username;
    public String   id;
    public String   ip;
    public int     port;
	private Socket tcpConn;
	private PrintWriter out;
	BufferedReader in;

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
        try
		{
			out = new PrintWriter(tcpConn.getOutputStream(), true);
	        in = new BufferedReader(new InputStreamReader(tcpConn.getInputStream()));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			log.printLogMessage(Log.ERROR, CLASS_ID, "unable to get a reader or writer for peer socket with username: " + username);
			e.printStackTrace();
		}
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
    
    /**
     * For integrity reasons do not use this method to read or write the socket.
     * Use ONLY for getting connection meta data
     * @return
     */
    public Socket getConn()
    {
    	return tcpConn;
    }
    
    public synchronized void sendMessage(Message msg)
    {
    	out.println(msg.toJsonString());
    	System.out.println("=======SENDING MESSAGE: \n" + msg.toJsonString());
    }
    
    public synchronized void setWriter(PrintWriter writer)
    {
    	out = writer;
    }
    
    public synchronized void setReader(BufferedReader reader)
    {
    	in = reader;
    }
    
    public BufferedReader getReader()
    {
    	return in;
    }
    
    /**
     * Wrapper class that reads a line from the socket.
     * Though it is synchronized only ONE place should read from it
     * @return
     */
    public synchronized String getNextLine()
    {
    	try
		{
			return in.readLine();
		}
		catch (IOException e)
		{
			log.printLogMessage(Log.ERROR, CLASS_ID, "Could not read from socket of peer: " + username);
			e.printStackTrace();
		}
		return "";
    }

	public String getInetString()
	{
		return ip + ":" + String.valueOf(port);
	}
	
	public String toJsonString()
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
