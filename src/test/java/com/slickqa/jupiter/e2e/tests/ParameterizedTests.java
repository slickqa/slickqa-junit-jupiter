package com.slickqa.jupiter.e2e.tests;

import com.slickqa.client.model.Result;
import com.slickqa.jupiter.ConfigurationNames;
import com.slickqa.jupiter.e2e.SlickJunitRunner;
import com.slickqa.jupiter.e2e.example.DataDrivenTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * These tests verify that parameterized tests (data driven) work with the slick junit jupiter plugin.
 */
public class ParameterizedTests {

    private SlickJunitRunner util;

    @BeforeEach
    public void setup() throws Exception {
        util = new SlickJunitRunner();
    }

    @Test
    @DisplayName("Each Instance of a Data Driven test is reported")
    public void testDataDrivenTestReportsEachInstance() throws Exception {
        Method dataDrivenTest = DataDrivenTests.class.getMethod("testSimpleParameterizedTest", String.class);
        List<Result> results = util.runDataDrivenTestMethod(dataDrivenTest, true);
        assertEquals(3, results.size(), "There should be 3 results, one for each of \"one\", \"two\", and \"three\"");
    }

    @Test
    @DisplayName("Each Instance of a Data Driven test has a unique AutomationID")
    public void testDataDrivenTestHasUniqueAutomationId() throws Exception {
        Method dataDrivenTest = DataDrivenTests.class.getMethod("testSimpleParameterizedTest", String.class);
        List<Result> results = util.runDataDrivenTestMethod(dataDrivenTest, true);
        Set<String> idSet = new HashSet<>();
        for(Result result : results) {
            idSet.add(result.getTestcase().getAutomationId());
        }
        assertEquals(3, idSet.size(), "There should be 3 separate ID's in the set of result automationIds.");
    }

    @Test
    @DisplayName("Using Custom ParameterizedTest Attribute causes attribute with parameter hash to be logged")
    public void testDataDrivenTestWithCustomAnnotationHasParameterHashInAttributes() throws Exception {
        String previousSetting = System.getProperty(ConfigurationNames.SCHEDULE_TEST_MODE);
        System.setProperty(ConfigurationNames.SCHEDULE_TEST_MODE, "true");
        try {
            Method dataDrivenTest = DataDrivenTests.class.getMethod("testCustomParameterizedTest", String.class);
            List<Result> results = util.runDataDrivenTestMethod(dataDrivenTest, true);
            for (Result result : results) {
                assertNotNull(result.getAttributes(), "The result should have attributes.");
                assertTrue(result.getAttributes().containsKey("jupiterArgumentsChecksum"), "There should be a jupiterArgumentsChecksum in the attributes.");
            }
        } finally {
            if(previousSetting == null)
                System.setProperty(ConfigurationNames.SCHEDULE_TEST_MODE, "false");
            else
                System.setProperty(ConfigurationNames.SCHEDULE_TEST_MODE, previousSetting);
        }
    }

    @Test
    @DisplayName("Using Custom ParameterizedTest Attribute causes parameter hashes to be checked")
    public void testDataDrivenTestWithCustomAnnotationChecksAttributeHash() throws Exception {
    }

}
