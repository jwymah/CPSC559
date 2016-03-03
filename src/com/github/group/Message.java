/**
 *  Message.java
 *
 *  @author  Cory Hutchison
 *  @author  Jeremy Mah
 *  @author  Frankie Yuan
 */ 
package com.github.group;

public class Message {

    public int timestamp;

    /**
     * Constructor
     */
    public Message() {
        timestamp = (int) (System.currentTimeMillis() / 1000L);
    }

}
