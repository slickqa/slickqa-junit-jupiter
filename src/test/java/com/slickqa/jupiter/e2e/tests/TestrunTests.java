package com.slickqa.jupiter.e2e.tests;

import com.slickqa.client.model.Result;
import com.slickqa.client.model.Testrun;
import com.slickqa.jupiter.e2e.SlickJunitRunner;
import com.slickqa.jupiter.e2e.example.ExampleTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestrunTests {

    private SlickJunitRunner util;

    @BeforeEach
    public void setup() throws Exception {
        util = new SlickJunitRunner();
    }

    @Test
    @DisplayName("TestrunTests started timestamp if not set when result starts")
    public void testTestrunSetStartedWhenNeeded() throws Exception {
        Method test = ExampleTest.class.getMethod("examplePassTest");
        Result result = util.runTestMethod(test);
        Testrun testrun = util.slick.testrun(result.getTestrun().getTestrunId()).get();
        assertNotNull(testrun.getRunStarted(), "The started timestamp should not be null");
    }

}
