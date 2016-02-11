package com.github.cjhutchi;

import org.junit.Test;
import org.junit.Assert;

public class CommandTest {

    @Test
    public void assertTrueTest () {
        Assert.assertTrue("this is true", true);
    }

    @Test
    public void basicTest () {
        Assert.assertNull("This will be null", null);
        Assert.assertTrue("This is going to be true", true);
    }

}
