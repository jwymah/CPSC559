/**
 *  P2PChat.java
 *
 *  @author Cory Hutchison
 *  @author Frankie Yuan
 *  @author Jeremy Mah
 */
package com.github.group;

import java.util.Random;

import controlMessages.ControlMessage;
import controlMessages.MetadatasReq;

public class P2PChat {

    private final static String CLASS_ID = "P2PChat";
    private static Log log;
    private static Crypto c;

    public static String    username;
    public static String    id;

    /**
     * MAIN
     */
    public static void main (String args[]) {
        // Get instances of Crypto and Log
        log = Log.getInstance();
        c = Crypto.getInstance();

        // Get Port, Username and ID
        Random r = new Random();
        username = String.valueOf(r.nextInt(10));//"cjhutchi-" + c.getID();
        id = c.getID();

        // Start services
        log.printLogMessage(Log.INFO, CLASS_ID, "Starting NodeServer");
        NodeServer ns = NodeServer.getInstance();	// init
        PeerList.getInstance();	    // init
        GroupList.getInstance();    // init
        log.printLogMessage(Log.INFO, CLASS_ID, "Starting BroadcastServer");
        (new BroadcastServer()).start();
        log.printLogMessage(Log.INFO, CLASS_ID, "Starting BroadcastClient");
        (new BroadcastClient()).start();

        // allow for connection setup
        try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        getGroupSeed();
        
        (new Shell(ns)).start();
    }
    
    public static void getGroupSeed()
    {
    	try
    	{
	        //receive a seed of group metadata
	        MetadatasReq mdr = new MetadatasReq();
			ControlMessage mdrMessage = new ControlMessage();
			mdrMessage.setMsgBody(mdr.toJsonString());
			
			Peer peer = PeerList.getAPeer();
			if(peer != null)
			{
				peer.sendMessage(mdrMessage);
			}
    	}
    	catch (Exception e)
    	{
    		
    	}
    }
}
