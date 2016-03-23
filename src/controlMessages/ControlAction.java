package controlMessages;

/**
 * An enumeration of the valid MessageTypes that should be used/parsed in the JSON messages *
 */
public enum ControlAction {
    JOIN("join"),
    LEAVE("leave"),
    DUMPREQ("dumpreq"),
    DUMPRESP("dumpresp"),
    ;

    private final String type;

    /**
     * @param text
     */
    private ControlAction(final String type) {
        this.type = type;
    }

    /** 
     * @return String - the string representation of the ControlAction.type
     */
    @Override
    public String toString() {
        return type;
    }
    
    public static ControlAction fromString(String s)
    {
    	for (ControlAction t : ControlAction.values()) {
    		if (s.equalsIgnoreCase(t.toString())) {
    			return t;
    		}
    	}
		return null;
    }
}
