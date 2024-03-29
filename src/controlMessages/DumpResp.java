/**
 *  ControlMessage.java
 *
 */
package controlMessages;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.github.group.Group;
import com.github.group.Log;


public class DumpResp {

    private static final String CLASS_ID = "Dump";
    private static final String DUMP = "dumpresp";
    private static final String ACTION = "action";
    private static final String TARGET_GROUP_ID = "targetgroupid";
    private static final String MEMBER_DUMP = "memberdump";
    private static final String TRY_LATER = "trylater";
    private static Log log;
    private JSONObject actionDetails;
    private String targetGroup;
    private JSONArray memberDump;
    private String trylater;

    /**
     * Constructor
     *
     * @param group incoming group
     */
    public DumpResp(Group group) {
        log = Log.getInstance();

        // Package in JSON object
        actionDetails = new JSONObject();
        actionDetails.put(ACTION, DUMP);
        actionDetails.put(TARGET_GROUP_ID, group.getId());
        JSONArray array = new JSONArray();
        for (String m : group.getMembersIds())
        {
            array.add(m);
        }
        actionDetails.put(MEMBER_DUMP, array);
    }

    /**
     * Constructor that parses and input message
     * @param m the input message
     */
    public DumpResp(String m) {
        // Remove weird added whitespace that rekt parsing
        // and initialize JSON parser
        m = m.trim();
        JSONParser parser = new JSONParser();

        // Parse message and get message components
        try {
            Object obj = parser.parse(m);

            actionDetails = (JSONObject) obj;
            targetGroup = (String) actionDetails.get(TARGET_GROUP_ID);
            memberDump = (JSONArray) actionDetails.get(MEMBER_DUMP);
            trylater = (String) actionDetails.get(TRY_LATER);
        } catch (ParseException e) {
            log.printLogMessage(Log.ERROR, CLASS_ID, "Received invalid Dump action");
            System.out.println(m);
        }
    }
    
    public void setTryLater()
    {
    	actionDetails.put(TRY_LATER, "true");
    }
    
    public boolean isValidDump()
    {
    	return (trylater == null);
    }
    
    public String getTargetGroup()
    {
        return targetGroup;
    }

    public JSONArray getMemberDump()
    {
        return memberDump;
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
