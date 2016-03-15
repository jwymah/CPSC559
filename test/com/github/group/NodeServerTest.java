package com.github.group;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NodeServerTest {

    static NodeServer n = null;

    @Before
    public void setUp() {
        n = NodeServer.getInstance();
    }

    @After
    public void tearDown() {
        n.shutdown();
        n = null;
    }

    @Test
    public void testMessageServer() {
        Assert.assertNotNull(m);
    }

    @Test
    public void testGetPort() {
        int i = n.getPort();
        Assert.assertTrue((i >= 9000 && i <= 10000));
    }

}
