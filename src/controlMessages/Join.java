/**
 *  ControlMessage.java
 *
 */
package controlMessages;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
    public Join() {
    	super();
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
            groupName = (String) actionDetails.get(GROUP_NAME);
        } catch (ParseException e) {
            log.printLogMessage(Log.ERROR, CLASS_ID, "Received invalid ChatMessage");
            System.out.println(m);
        }
    }
    
    public void setExternalContact(String contactId)
    {
    	externalContactId = contactId;
    }
    
    public String getExternalContact()
    {
    	return externalContactId;
    }
    
    public void setGroupName(String name)
    {
    	groupName = name;
    }
    
    public String getGroupName()
    {
    	return groupName;
    }
    
    public void setTargetGroup(String target)
    {
    	targetGroup = target;
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
