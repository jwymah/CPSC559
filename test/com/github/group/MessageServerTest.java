package com.github.group;

import org.junit.Assert;
import org.junit.Test;

public class MessageServerTest {

    @Test
    public void testMessageServer() {
        MessageServer m = new MessageServer(9000);
        Assert.assertNotNull(m);
        m.isRunning = false;
    }

}
