/**
 *  ControlMessage.java
 *
 */
package controlMessages;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.github.group.GroupList;
import com.github.group.Log;

		
public class MetadatasDump {

    private static final String CLASS_ID = "MetadatasDump";
    private static final String METADATASDUMP = "metadatadump";
    private static final String ACTION = "action";
    private static Log log;
    private JSONObject actionDetails;
	private JSONArray metadatas;

    /**
     * Constructor
     */
    public MetadatasDump() {
        log = Log.getInstance();

        // Package in JSON object
        actionDetails = new JSONObject();
        actionDetails.put(ACTION, METADATASDUMP);
        actionDetails.put(METADATASDUMP, GroupList.getInstance().getAllMetadata());
        System.out.println("++++++++++++++++++++++++");
        System.out.println(GroupList.getInstance().getAllMetadata().toJSONString());
        System.out.println("++++++++++++++++++++++++");
    }    

    /**
     * Constructor that parses and input message
     */
    public MetadatasDump(String m) {
        // Remove weird added whitespace that rekt parsing
        // and initialize JSON parser
        m = m.trim();   
        JSONParser parser = new JSONParser();

        // Parse message and get message components
        try {
            Object obj = parser.parse(m);

            actionDetails = (JSONObject) obj;
            metadatas = (JSONArray) actionDetails.get(METADATASDUMP);
        } catch (ParseException e) {
            log.printLogMessage(Log.ERROR, CLASS_ID, "Received invalid Dump action");
            System.out.println(m);
        }
    }
    
    public JSONArray getMetadatas()
    {
    	return metadatas;
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
