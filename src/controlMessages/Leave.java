/**
 *  Leave.java
 *
 */
package controlMessages;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.github.group.Group;
import com.github.group.Log;

		
public class Leave {

    private static final String CLASS_ID = "Leave";
    private static final String LEAVE = "leave";
    private static final String ACTION = "action";
    private static final String TARGET_GROUP_ID = "targetgroupid";
    private static Log log;
    private JSONObject actionDetails;
    private String targetGroup;

    /**
     * Constructor
     */
    public Leave(Group group) {
        log = Log.getInstance();

        // Package in JSON object
        actionDetails = new JSONObject();
        actionDetails.put(ACTION, LEAVE);
        actionDetails.put(TARGET_GROUP_ID, group.getId());
    }    

    /**
     * Constructor that parses and input message
     */
    public Leave(String m) {
        // Remove weird added whitespace that rekt parsing
        // and initialize JSON parser
        m = m.trim();   
        JSONParser parser = new JSONParser();

        // Parse message and get message components
        try {
            Object obj = parser.parse(m);

            actionDetails = (JSONObject) obj;
            targetGroup = (String) actionDetails.get(TARGET_GROUP_ID);
        } catch (ParseException e) {
            log.printLogMessage(Log.ERROR, CLASS_ID, "Received invalid Leave Action");
            System.out.println(m);
        }
    }
    
    public String getTargetGroup()
    {
    	return targetGroup;
    }

    /**
     * Returns a Leave action as JSON string format
     *
     * @return JSON ChatMessage as string
     */
	public String toJsonString()
	{
		return actionDetails.toJSONString();
	}
}
