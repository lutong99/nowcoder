package org.example.nowcoder.api;

import org.junit.jupiter.api.Test;

public class StringTests {

    @Test
    public void testString() {
        Object testObject = getObject();
        System.out.println(String.valueOf(testObject));

    }

    public Object getObject() {
        return "Hello world";
    }

}
