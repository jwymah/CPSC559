package com.github.group;

import org.junit.Test;
import org.junit.Assert;

public class MessageTest {
    Message msg = new Message("HOST", "GROUP", "TYPE", "MSG");

    @Test
    public void testMessage() {
        Assert.assertNotNull(msg);
    }

    @Test
    public void testTimeStamp() {
        Message msg2 = new Message("nil", "nil", "nil", "nil");
        Assert.assertNotSame(msg.getTimeStamp(), msg2.getTimeStamp());
    }

    @Test
    public void testGetHostID() {
        String s = msg.getHostID();
        Assert.assertEquals(s, "HOST");
    }

    @Test
    public void testGetGroupID() {
        String s = msg.getGroupID();
        Assert.assertEquals(s, "GROUP");
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

}
