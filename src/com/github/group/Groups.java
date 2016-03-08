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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Groups {

    public static Map<String, Group> map;
    private static Groups instance = null;

    /**
     * Constructor
     */
    protected Groups() {

        map = new HashMap<String, Group>();
    }

    /**
     * Returns an instance of Groups
     * 
     * @return An instance of Groups
     */
    public static Groups getInstance() {

        if (instance == null) {
            instance = new Groups();
        } 

        return instance;

    }

}
