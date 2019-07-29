package com.slickqa.jupiter.e2e.tests;

import com.slickqa.client.model.LogEntry;
import com.slickqa.client.model.Result;
import com.slickqa.client.model.Testcase;
import com.slickqa.jupiter.annotations.SlickMetaData;
import com.slickqa.jupiter.annotations.Step;
import com.slickqa.jupiter.e2e.SlickJunitRunner;
import com.slickqa.jupiter.e2e.example.MetaDataTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MetaDataAccuracyTests {
    private SlickJunitRunner util;

    @BeforeEach
    public void setup() throws Exception {
        util = new SlickJunitRunner();
    }


    @Test
    @DisplayName("Test name comes from SlickMetaData annotation")
    public void testNameComesFromSlickMetaData() throws Exception {
        Method test = MetaDataTests.class.getMethod("testSlickMetaData");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        SlickMetaData info = test.getDeclaredAnnotation(SlickMetaData.class);
        assertEquals(info.title(), result.getTestcase().getName(), "name of test should come from SlickMetaData");
    }

    @Test
    @DisplayName("Test name can come from DisplayName annotation")
    public void testNameCanComeFromDisplayName() throws Exception {
        Method test = MetaDataTests.class.getMethod("testDisplayName");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        DisplayName name = test.getDeclaredAnnotation(DisplayName.class);
        assertEquals(name.value(), result.getTestcase().getName(), "name of test should come from DisplayName");
    }

    @Test
    @DisplayName("Test name comes from SlickMetaData when both SlickMetaData and DisplayName are present")
    public void testNameComesFromSlickMetaDataWhenBothArePresent() throws Exception {
        Method test = MetaDataTests.class.getMethod("testDisplayNameAndSlickMetaData");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        SlickMetaData info = test.getDeclaredAnnotation(SlickMetaData.class);
        assertEquals(info.title(), result.getTestcase().getName(), "name of test should come from SlickMetaData");
    }

    @Test
    @DisplayName("Test name comes from DisplayName when SlickMetaData title is empty.")
    public void testNameComesFromDisplayNameWhenSlickMetaDataIsEmpty() throws Exception {
        Method test = MetaDataTests.class.getMethod("testDisplayNameEmptyMetadataName");
        SlickMetaData info = test.getDeclaredAnnotation(SlickMetaData.class);
        assertEquals("", info.title(), "The title in SlickMetaData should be empty (default).  If this is not true then somebody messed up the test.");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        DisplayName displayName = test.getDeclaredAnnotation(DisplayName.class);
        assertEquals(displayName.value(), result.getTestcase().getName(), "name of the test should come from DisplayName as SlickMetaData's title is empty");
    }

    @Test
    @DisplayName("Test name comes from method name when no annotation is present")
    public void testNameComesFromMethodNameWhenNoAnnotationIsPresent() throws Exception {
        Method test = MetaDataTests.class.getMethod("testMethodNameOnly");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        assertEquals(test.getName(), result.getTestcase().getName(), "name of test should come from the method name");
    }

    @Test
    @DisplayName("Component from SlickMetaData")
    public void testComponentFromSlickMetaData() throws Exception {
        Method test = MetaDataTests.class.getMethod("testSlickMetaData");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        Testcase slickTestcase = util.slick.testcase(result.getTestcase().getTestcaseId()).get();
        SlickMetaData info = test.getDeclaredAnnotation(SlickMetaData.class);
        assertEquals(info.component(), result.getComponent().getName(), "Component of result should come from SlickMetaData");
        assertEquals(info.component(), slickTestcase.getComponent().getName(), "Component of test case should have come from SlickMetaData");
    }

    @Test
    @DisplayName("Feature from SlickMetaData")
    public void testFeatureFromSlickMetaData() throws Exception {
        Method test = MetaDataTests.class.getMethod("testSlickMetaData");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        Testcase slickTestcase = util.slick.testcase(result.getTestcase().getTestcaseId()).get();
        SlickMetaData info = test.getDeclaredAnnotation(SlickMetaData.class);
        assertEquals(info.feature(), slickTestcase.getFeature().getName(), "Feature of test should come from SlickMetaData");
    }

    @Test
    @DisplayName("Steps from SlickMetaData")
    public void testStepsFromSlickMetaData() throws Exception {
        Method test = MetaDataTests.class.getMethod("testSlickMetaData");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        Testcase slickTestcase = util.slick.testcase(result.getTestcase().getTestcaseId()).get();
        SlickMetaData info = test.getDeclaredAnnotation(SlickMetaData.class);
        int i = 0;
        for(Step step : info.steps()) {
            assertEquals(step.step(), slickTestcase.getSteps().get(i).getName(), "Step " + i + " should have matched what was in SlickMetaData");
            assertEquals(step.expectation(), slickTestcase.getSteps().get(i).getExpectedResult(), "Step " + i + "'s Expected Result should have matched what was in SlickMetaData");
            i++;
        }
    }

    @Test
    @DisplayName("Author from SlickMetaData")
    public void testAuthorFromSlickMetaData() throws Exception {
        Method test = MetaDataTests.class.getMethod("testSlickMetaData");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        Testcase slickTestcase = util.slick.testcase(result.getTestcase().getTestcaseId()).get();
        SlickMetaData info = test.getDeclaredAnnotation(SlickMetaData.class);
        assertEquals(info.author(), slickTestcase.getAuthor(), "Author of test should come from SlickMetaData");
    }

    @Test
    @DisplayName("Purpose from SlickMetaData")
    public void testPurposeFromSlickMetaData() throws Exception {
        Method test = MetaDataTests.class.getMethod("testSlickMetaData");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        SlickMetaData info = test.getDeclaredAnnotation(SlickMetaData.class);
        Testcase slickTestcase = util.slick.testcase(result.getTestcase().getTestcaseId()).get();
        assertEquals(info.purpose(), slickTestcase.getPurpose(), "Purpose of test should come from SlickMetaData");
    }

    @Test
    @DisplayName("AutomationId from SlickMetaData")
    public void testAutomationIdFromSlickMetaData() throws Exception {
        Method test = MetaDataTests.class.getMethod("testSlickMetaData");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        SlickMetaData info = test.getDeclaredAnnotation(SlickMetaData.class);
        assertEquals(info.automationId(), result.getTestcase().getAutomationId(), "AutomationId of test should come from SlickMetaData");
    }

    @Test
    @DisplayName("AutomationKey from SlickMetaData")
    public void testAutomationKeyFromSlickMetaData() throws Exception {
        Method test = MetaDataTests.class.getMethod("testSlickMetaData");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        SlickMetaData info = test.getDeclaredAnnotation(SlickMetaData.class);
        assertEquals(info.automationKey(), result.getTestcase().getAutomationKey(), "AutomationKey of test should come from SlickMetaData");
    }

    @Test
    @DisplayName("TriageNote from SlickMetaData")
    public void testTriageNoteFromSlickMetaData() throws Exception {
        Method test = MetaDataTests.class.getMethod("testTriageNote");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        SlickMetaData info = test.getDeclaredAnnotation(SlickMetaData.class);
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
