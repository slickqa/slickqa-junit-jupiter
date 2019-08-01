package com.slickqa.jupiter.e2e.tests;

import com.slickqa.client.model.Result;
import com.slickqa.client.model.Testrun;
import com.slickqa.jupiter.ConfigurationNames;
import com.slickqa.jupiter.DefaultAttributes;
import com.slickqa.jupiter.e2e.SlickJunitRunner;
import com.slickqa.jupiter.e2e.example.ExampleTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class SchedulingTests {

    private SlickJunitRunner util;
    private String schedulingState;

    @BeforeEach
    public void setup() throws Exception {
        util = new SlickJunitRunner();
        DefaultAttributes.wipeCache();
        schedulingState = System.getProperty(ConfigurationNames.SCHEDULE_TEST_MODE);
        System.setProperty(ConfigurationNames.SCHEDULE_TEST_MODE, "true");
    }

    @AfterEach
    public void cleanup() throws Exception {
        if(schedulingState != null) {
            System.setProperty(ConfigurationNames.SCHEDULE_TEST_MODE, schedulingState);
        } else {
            System.setProperty(ConfigurationNames.SCHEDULE_TEST_MODE, "false");
        }
    }

    @Test
    @DisplayName("Scheduling tests put them in the scheduled state.")
    public void testSchedulingCreatesTestInProperState() throws Exception {
        Method test = ExampleTest.class.getMethod("examplePassTest");
        Result result = util.runTestMethod(test);
        assertEquals("SCHEDULED", result.getRunstatus(), "Runstatus should be scheduled.");
        assertEquals("NO_RESULT", result.getStatus(), "Status should be NO_RESULT");
    }

    @Test
    @DisplayName("Scheduling tests adds the scheduled attribute to the result and the testrun.")
    public void testSchedulingAddsScheduledAttribute() throws Exception {
        Method test = ExampleTest.class.getMethod("examplePassTest");
        Result result = util.runTestMethod(test);
        assertNotNull(result.getAttributes(), "Result's attributes should not be null");
        assertTrue(result.getAttributes().containsKey("scheduled"),
                   "Result attributes should contain a scheduled attribute." +
                   "It didn't, available attributes are: " +
                   Arrays.toString(result.getAttributes().keySet().toArray()));
        Testrun testrun = util.slick.testrun(result.getTestrun().getTestrunId()).get();
        assertNotNull(testrun, "Testrun should be available, uh-oh");
        assertNotNull(testrun.getAttributes(), "Testrun should have attributes, it did not.");
        assertTrue(testrun.getAttributes().containsKey("scheduled"),
                   "Testrun should have had a scheduled attribute.  Available attributes are: " +
                   Arrays.toString(testrun.getAttributes().keySet().toArray()));
    }

}
