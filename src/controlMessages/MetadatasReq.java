/**
 *  MetadataReq.java
 *
 */
package controlMessages;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.github.group.Log;

		
public class MetadatasReq {

    private static final String CLASS_ID = "MetadatasReq";
    private static final String METADATASREQ = "metadatareq";
    private static final String ACTION = "action";
    private static Log log;
    private JSONObject actionDetails;

    /**
     * Constructor
     */
    public MetadatasReq() {
        log = Log.getInstance();

        // Package in JSON object
        actionDetails = new JSONObject();
        actionDetails.put(ACTION, METADATASREQ);
    }    

    /**
     * Constructor that parses and input message
     * @param m the input message
     */
    public MetadatasReq(String m) {
        // Remove weird added whitespace that rekt parsing
        // and initialize JSON parser
        m = m.trim();   
        JSONParser parser = new JSONParser();

        // Parse message and get message components
        try {
            Object obj = parser.parse(m);

            actionDetails = (JSONObject) obj;
            
        } catch (ParseException e) {
            log.printLogMessage(Log.ERROR, CLASS_ID, "Received invalid DumpReq action");
            System.out.println(m);
        }
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
