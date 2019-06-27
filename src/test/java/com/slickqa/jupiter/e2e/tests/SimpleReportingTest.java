package com.slickqa.jupiter.e2e.tests;

import com.slickqa.client.SlickClient;
import com.slickqa.client.SlickClientFactory;
import com.slickqa.client.model.Result;
import com.slickqa.jupiter.annotations.SlickMetaData;
import com.slickqa.jupiter.e2e.SlickJunitRunner;
import com.slickqa.jupiter.e2e.example.ExampleTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Simple Reporting Tests")
public class SimpleReportingTest {
    private SlickJunitRunner util;

    @BeforeEach
    public void setup() throws Exception {
        util = new SlickJunitRunner();
    }

    @Test
    @DisplayName("Test name reported correctly")
    public void testNameComesFromSlickMetaData() throws Exception {
        Method test = ExampleTest.class.getMethod("examplePassTest");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        SlickMetaData info = test.getDeclaredAnnotation(SlickMetaData.class);
        assertEquals(info.title(), result.getTestcase().getName(), "Test name should be the same as from SlickMetaData");
    }

}
