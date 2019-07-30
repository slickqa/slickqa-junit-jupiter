package com.slickqa.jupiter.e2e.tests;

import com.slickqa.client.model.LogEntry;
import com.slickqa.client.model.Result;
import com.slickqa.client.model.Testcase;
import com.slickqa.jupiter.annotations.TestCaseInfo;
import com.slickqa.jupiter.annotations.Step;
import com.slickqa.jupiter.e2e.SlickJunitRunner;
import com.slickqa.jupiter.e2e.example.TestCaseInfoTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestCaseInfoAccuracyTests {
    private SlickJunitRunner util;

    @BeforeEach
    public void setup() throws Exception {
        util = new SlickJunitRunner();
    }


    @Test
    @DisplayName("Test name comes from TestCaseInfo annotation")
    public void testNameComesFromTestcaseInfo() throws Exception {
        Method test = TestCaseInfoTests.class.getMethod("testTestcaseInfo");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        TestCaseInfo info = test.getDeclaredAnnotation(TestCaseInfo.class);
        assertEquals(info.title(), result.getTestcase().getName(), "name of test should come from TestCaseInfo");
    }

    @Test
    @DisplayName("Test name can come from DisplayName annotation")
    public void testNameCanComeFromDisplayName() throws Exception {
        Method test = TestCaseInfoTests.class.getMethod("testDisplayName");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        DisplayName name = test.getDeclaredAnnotation(DisplayName.class);
        assertEquals(name.value(), result.getTestcase().getName(), "name of test should come from DisplayName");
    }

    @Test
    @DisplayName("Test name comes from TestCaseInfo when both TestCaseInfo and DisplayName are present")
    public void testNameComesFromTestcaseInfoWhenBothArePresent() throws Exception {
        Method test = TestCaseInfoTests.class.getMethod("testDisplayNameAndTestcaseInfo");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        TestCaseInfo info = test.getDeclaredAnnotation(TestCaseInfo.class);
        assertEquals(info.title(), result.getTestcase().getName(), "name of test should come from TestCaseInfo");
    }

    @Test
    @DisplayName("Test name comes from DisplayName when TestCaseInfo title is empty.")
    public void testNameComesFromDisplayNameWhenTestcaseInfoIsEmpty() throws Exception {
        Method test = TestCaseInfoTests.class.getMethod("testDisplayNameEmptyTestcaseInfoName");
        TestCaseInfo info = test.getDeclaredAnnotation(TestCaseInfo.class);
        assertEquals("", info.title(), "The title in TestCaseInfo should be empty (default).  If this is not true then somebody messed up the test.");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        DisplayName displayName = test.getDeclaredAnnotation(DisplayName.class);
        assertEquals(displayName.value(), result.getTestcase().getName(), "name of the test should come from DisplayName as TestCaseInfo's title is empty");
    }

    @Test
    @DisplayName("Test name comes from method name when no annotation is present")
    public void testNameComesFromMethodNameWhenNoAnnotationIsPresent() throws Exception {
        Method test = TestCaseInfoTests.class.getMethod("testMethodNameOnly");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        assertEquals(test.getName() + "()", result.getTestcase().getName(), "name of test should come from the method name");
    }

    @Test
    @DisplayName("Test that the templated name is given to slick")
    public void testTemplatedName() throws Exception {
        Method test = TestCaseInfoTests.class.getMethod("testTemplatedName", String.class);
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        assertEquals("Test Yomama", result.getTestcase().getName(), "name should be the templated name.");
    }

    @Test
    @DisplayName("Component from TestCaseInfo")
    public void testComponentFromTestcaseInfo() throws Exception {
        Method test = TestCaseInfoTests.class.getMethod("testTestcaseInfo");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        Testcase slickTestcase = util.slick.testcase(result.getTestcase().getTestcaseId()).get();
        TestCaseInfo info = test.getDeclaredAnnotation(TestCaseInfo.class);
        assertEquals(info.component(), result.getComponent().getName(), "Component of result should come from TestCaseInfo");
        assertEquals(info.component(), slickTestcase.getComponent().getName(), "Component of test case should have come from TestCaseInfo");
    }

    @Test
    @DisplayName("Feature from TestCaseInfo")
    public void testFeatureFromTestcaseInfo() throws Exception {
        Method test = TestCaseInfoTests.class.getMethod("testTestcaseInfo");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        Testcase slickTestcase = util.slick.testcase(result.getTestcase().getTestcaseId()).get();
        TestCaseInfo info = test.getDeclaredAnnotation(TestCaseInfo.class);
        assertEquals(info.feature(), slickTestcase.getFeature().getName(), "Feature of test should come from TestCaseInfo");
    }

    @Test
    @DisplayName("Steps from TestCaseInfo")
    public void testStepsFromTestcaseInfo() throws Exception {
        Method test = TestCaseInfoTests.class.getMethod("testTestcaseInfo");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        Testcase slickTestcase = util.slick.testcase(result.getTestcase().getTestcaseId()).get();
        TestCaseInfo info = test.getDeclaredAnnotation(TestCaseInfo.class);
        int i = 0;
        for(Step step : info.steps()) {
            assertEquals(step.step(), slickTestcase.getSteps().get(i).getName(), "Step " + i + " should have matched what was in TestCaseInfo");
            assertEquals(step.expectation(), slickTestcase.getSteps().get(i).getExpectedResult(), "Step " + i + "'s Expected Result should have matched what was in TestCaseInfo");
            i++;
        }
    }

    @Test
    @DisplayName("Author from TestCaseInfo")
    public void testAuthorFromTestcaseInfo() throws Exception {
        Method test = TestCaseInfoTests.class.getMethod("testTestcaseInfo");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        Testcase slickTestcase = util.slick.testcase(result.getTestcase().getTestcaseId()).get();
        TestCaseInfo info = test.getDeclaredAnnotation(TestCaseInfo.class);
        assertEquals(info.author(), slickTestcase.getAuthor(), "Author of test should come from TestCaseInfo");
    }

    @Test
    @DisplayName("Purpose from TestCaseInfo")
    public void testPurposeFromTestcaseInfo() throws Exception {
        Method test = TestCaseInfoTests.class.getMethod("testTestcaseInfo");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        TestCaseInfo info = test.getDeclaredAnnotation(TestCaseInfo.class);
        Testcase slickTestcase = util.slick.testcase(result.getTestcase().getTestcaseId()).get();
        assertEquals(info.purpose(), slickTestcase.getPurpose(), "Purpose of test should come from TestCaseInfo");
    }

    @Test
    @DisplayName("AutomationId from TestCaseInfo")
    public void testAutomationIdFromTestcaseInfo() throws Exception {
        Method test = TestCaseInfoTests.class.getMethod("testTestcaseInfo");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        TestCaseInfo info = test.getDeclaredAnnotation(TestCaseInfo.class);
        assertEquals(info.automationId(), result.getTestcase().getAutomationId(), "AutomationId of test should come from TestCaseInfo");
    }

    @Test
    @DisplayName("AutomationKey from TestCaseInfo")
    public void testAutomationKeyFromTestcaseInfo() throws Exception {
        Method test = TestCaseInfoTests.class.getMethod("testTestcaseInfo");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        TestCaseInfo info = test.getDeclaredAnnotation(TestCaseInfo.class);
        assertEquals(info.automationKey(), result.getTestcase().getAutomationKey(), "AutomationKey of test should come from TestCaseInfo");
    }

    @Test
    @DisplayName("TriageNote from TestCaseInfo")
    public void testTriageNoteFromTestcaseInfo() throws Exception {
        Method test = TestCaseInfoTests.class.getMethod("testTriageNote");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        TestCaseInfo info = test.getDeclaredAnnotation(TestCaseInfo.class);
        LogEntry triageNote = null;
        for (LogEntry l: result.getLog()
             ) {
            if ("slick.note".equals(l.getLoggerName())){
                triageNote = l;
                break;
            }
        }
        assertNotNull(triageNote, "Didn't find a triage note on result");
        assertEquals(triageNote.getMessage(), "Yomama");
    }
}
