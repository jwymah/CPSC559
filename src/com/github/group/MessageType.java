package com.github.group;

/**
 * An enumeration of the valid MessageTypes that should be used/parsed in the JSON messages *
 */
public enum MessageType {
	BLANK("BLANK_TYPE"),
    BROADCAST("Broadcast"),
    CHAT("Chat"),
    CONTROL("Control"),
    QUERY("Query"),
    QUERY_RESPONSE("Query response")
    ;

    private final String type;

    /**
     * @param text
     */
    private MessageType(final String type) {
        this.type = type;
    }

    /** 
     * @return String - the string representation of the MessageType.type
     */
    @Override
    public String toString() {
        return type;
    }
    
    public static MessageType fromString(String s)
    {
    	for (MessageType t : MessageType.values()) {
    		if (s.equalsIgnoreCase(t.toString())) {
    			return t;
    		}
    	}
		return BLANK;
    }
}
