/**
 *  QueryResponseMessage.java
 *
 *  @author Cory Hutchison
 *  @author Frankie Yuan
 *  @author Jeremy Mah
 */
package com.github.group;

public class QueryResponseMessage extends Message {

    private final static String CLASS_ID = "QueryResponseMessage";
    private static Log log;

    /**
     * Constructor
     */
    public QueryResponseMessage() {
        super(MessageType.QUERY_RESPONSE);
        log = Log.getInstance();
    }
}
