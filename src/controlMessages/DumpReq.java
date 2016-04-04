/**
 *  DumpReq.java
 *
 */
package controlMessages;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.github.group.Group;
import com.github.group.Log;

		
public class DumpReq {

    private static final String CLASS_ID = "Dump";
    private static final String DUMP = "dumpreq";
    private static final String ACTION = "action";
    private static final String TARGET_GROUP_ID = "targetgroupid";
    private static Log log;
    private JSONObject actionDetails;
    private String targetGroup;

    /**
     * Constructor
     */
    public DumpReq(Group group) {
        log = Log.getInstance();

        // Package in JSON object
        actionDetails = new JSONObject();
        actionDetails.put(ACTION, DUMP);
        actionDetails.put(TARGET_GROUP_ID, group.getId());
    }    

    /**
     * Constructor that parses and input message
     */
    public DumpReq(String m) {
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
            log.printLogMessage(Log.ERROR, CLASS_ID, "Received invalid Dump action");
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
