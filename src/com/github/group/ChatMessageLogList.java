/**
 *  ChatMessageLog.java
 *
 *  @author Cory Hutchison
 *  @author Frankie Yuan
 *  @author Jeremy Mah
 */
package com.github.group;

import java.util.*;

/**
 * Collection of chat message logs, one for each group the peer is a part of.
 */
public class ChatMessageLogList {

    private static ChatMessageLogList instance = null;

    // each group has its own message log
    public static Map<String,ChatMessageLog> chatMessageLogs;

    /**
     * Constructor
     */
    public ChatMessageLogList() {
        chatMessageLogs = new HashMap<String,ChatMessageLog>();
    }

    /**
     * Returns an instance of ChatMessageLogList
     *
     * @return An instance of the ChatMessageLogList
     */
    public static ChatMessageLogList getInstance() {
        if (instance == null) {
            instance = new ChatMessageLogList();
        }
        return instance;
    }


    /**
     * adds message to log
     * creates new log for group if group did not exist in list
     * otherwise adds message to exciting log
     * @param message chat message
     * @param groupID group from which the chat message came from
     */
    public void addMessage(ChatMessage message, String groupID) {
        if(chatMessageLogs.containsKey(groupID)) {
            chatMessageLogs.get(groupID).addMessage(message);
        } else {
            ChatMessageLog c = new ChatMessageLog();
            c.addMessage(message);
            chatMessageLogs.put(groupID,c);
        }

    }
}
