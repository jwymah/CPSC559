package com.github.group;

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

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
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
