/**
 *  QueryMessage.java
 *
 *  @author Cory Hutchison
 *  @author Frankie Yuan
 *  @author Jeremy Mah
 */
package com.github.group;

public class QueryMessage extends Message {

    private final static String CLASS_ID = "QueryMessage";
    private static Log log;

    public String sourceIP;
    public String destIP;
    public String deskID;
    public String queryID;
    public String query;
    public String returnPath[];

    /**
     * Constructor
     */
    public QueryMessage() {
        super(MessageType.QUERY);
        log = Log.getInstance();
    }
}
