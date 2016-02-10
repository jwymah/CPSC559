package com.github.cjhutchi;

import org.junit.Test;

public class CommandTest extends junit.framework.TestCase {
    private Command c;

    protected void setUp () {
        c = new Command();
    }

    @Test 
    public void getOneTest() {
        System.out.println("In getOneTest:");
        int i = c.getOne();
        assertEquals(i,1);
    }

    @Test
    public void getHelloTest () {
        System.out.println("In getHelloTest:");
        String v = c.getHello();
        assertEquals(v,"Hello.");
    }

}
