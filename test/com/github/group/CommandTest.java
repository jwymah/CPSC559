package com.github.group;

import org.junit.Test;
import org.junit.Assert;

public class CommandTest {
    private Command c = new Command();

    @Test
    public void getOneTest () {
        int i = c.getOne();
        Assert.assertEquals(i, 1);
    }

    @Test
    public void getHelloTest () {
        String s = c.getHello();
        Assert.assertEquals(s, "Hello.");
    }

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
