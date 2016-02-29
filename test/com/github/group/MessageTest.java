package com.github.group;

import org.junit.Test;
import org.junit.Assert;

public class MessageTest {
    Message msg = new Message("ID", "nil", "TYPE", "MSG");

    @Test
    public void testGetHostID() {
        String s = msg.getHostID();
        Assert.assertEquals(s, "ID");
    }

    @Test
    public void testGetMessageType() {
        String s = msg.getMessageType();
        Assert.assertEquals(s, "TYPE");
    }

    @Test
    public void testGetMessageBody() {
        String s = msg.getMessageBody();
        Assert.assertEquals(s, "MSG");
    }

    @Test
    public void testGetGroupID() {
        String s = msg.getGroupID();
        Assert.assertEquals(s, "nil");
    }
}
