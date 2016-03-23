/**
 *  ControlMessage.java
 *
 */
package controlMessages;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.github.group.Group;
import com.github.group.Log;

		
public class Join{

    private static final String CLASS_ID = "Join";
    private static final String JOIN = "join";
    private static final String EXTERNAL_CONTACT_ID = "externalcontactid";
    private static final String GROUP_NAME = "groupname";
    private static final String ACTION = "action";
    private static final String TARGET_GROUP_ID = "targetgroupid";
    private static Log log;
    private JSONObject actionDetails;
    private String targetGroup;
    private String externalContactId;
    private String groupName;

    /**
     * Constructor
     */
    public Join(Group group) {
        log = Log.getInstance();

        // Package in JSON object
        actionDetails = new JSONObject();
        actionDetails.put(ACTION, JOIN);
        actionDetails.put(TARGET_GROUP_ID, group.getId());
        actionDetails.put(EXTERNAL_CONTACT_ID, group.getExternalContact());
        actionDetails.put(GROUP_NAME, group.getName());
    }    

    /**
     * Constructor that parses and input message
     */
    public Join(String m) {
        // Remove weird added whitespace that rekt parsing
        // and initialize JSON parser
        m = m.trim();   
        JSONParser parser = new JSONParser();

        // Parse message and get message components
        try {
            Object obj = parser.parse(m);

            actionDetails = (JSONObject) obj;
            targetGroup = (String) actionDetails.get(TARGET_GROUP_ID);
            groupName = (String) actionDetails.get(GROUP_NAME);
            externalContactId = (String) actionDetails.get(EXTERNAL_CONTACT_ID);
        } catch (ParseException e) {
            log.printLogMessage(Log.ERROR, CLASS_ID, "Received invalid ChatMessage");
            System.out.println(m);
        }
    }
    
    public String getExternalContact()
    {
    	return externalContactId;
    }
    
    public String getGroupName()
    {
    	return groupName;
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
