/**
 *  QueryMessage.java
 *
 *  @author Cory Hutchison
 *  @author Frankie Yuan
 *  @author Jeremy Mah
 */
package com.github.group;

public class QueryMessage extends Message {

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
        super();
    }
}
