package com.github.marceloleite2604;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class AppTest {

    @Test
    public void handleRequest_shouldReturnConstantValue() {
        App function = new App();
        Object result = function.handleRequest("echo", null);
        assertEquals("echo", result);
    }
}
