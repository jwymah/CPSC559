/**
 *  ChatMessageLog.java
 *
 *  @author Cory Hutchison
 *  @author Frankie Yuan
 *  @author Jeremy Mah
 */

package com.github.group;

import java.util.ArrayList;
import java.util.Collection;

public class ChatMessageLog {

    // collection of all messages sent in the group
    private Collection<ChatMessage> messages;

    /**
     * Constructor
     */
    public ChatMessageLog() {
        messages = new ArrayList<>();
    }

    /**
     * adds new chat message to log
     * @param msg chat message
     */
    public void addMessage(ChatMessage msg) {
        messages.add(msg);
    }

    public void printMessages()
    {
        for (ChatMessage m : messages)
        {
            System.out.println(m.getMsgBody());
        }
    }


}
