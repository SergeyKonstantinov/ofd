package com.sergey.tasks.parsers;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Sergey on 21.04.2019.
 */
public class HttpRequestParserTest {
    @Test
    public void parse() throws Exception {
        assertEquals(3, 1 + 2);
    }

    @Test
    public void parse2() throws Exception {
        assertTrue(2 > 0);
    }

    @Test
    public void parse3() throws Exception {
        System.out.println("Test3");
        assertTrue(2 > 0);
    }
}