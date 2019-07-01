package com.slickqa.jupiter.e2e.tests;

import com.slickqa.client.model.Result;
import com.slickqa.jupiter.annotations.SlickMetaData;
import com.slickqa.jupiter.e2e.SlickJunitRunner;
import com.slickqa.jupiter.e2e.example.ExampleTest;
import com.slickqa.jupiter.e2e.example.SetupThrowsExceptionTest;
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
    @DisplayName("Report Pass Result")
    public void testPassResult() throws Exception {
        Method test = ExampleTest.class.getMethod("examplePassTest");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        assertEquals("PASS", result.getStatus(), "Result of examplePassTest should be PASS");
    }

    @Test
    @DisplayName("Report Broken Result")
    public void testBrokenResult() throws Exception {
        Method test = ExampleTest.class.getMethod("shouldBeBrokenTest");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        assertEquals("BROKEN_TEST", result.getStatus(), "Result of shouldBeBrokenTest should be BROKEN_TEST");
    }

    @Test
    @DisplayName("Report Fail Result")
    public void testFailResult() throws Exception {
        Method test = ExampleTest.class.getMethod("testExampleFail");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        assertEquals("FAIL", result.getStatus(), "Result of testExampleFail should be FAIL");
    }

    @Test
    @DisplayName("Report Skipped Result")
    public void testSkippedResult() throws Exception {
        Method test = SetupThrowsExceptionTest.class.getMethod("testExceptionInSetupSkips");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        assertEquals("SKIPPED", result.getStatus(), "Result of testExceptionInSetupSkips should be SKIPPED");
    }

}
