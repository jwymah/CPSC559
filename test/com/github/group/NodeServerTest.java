package com.github.group;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NodeServerTest {

    static NodeServer m = null;

    @Before
    public void setUp() {
        m = NodeServer.getInstance();
    }

    @After
    public void tearDown() {
        m.shutdown();
        m = null;
    }

    @Test
    public void testNodeServer() {
        Assert.assertNotNull(m);
    }

    @Test
    public void testGetPort() {
        int i = m.getPort();
        Assert.assertTrue((i >= 9000 && i <= 10000));
    }

}
