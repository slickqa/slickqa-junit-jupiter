package com.slickqa.jupiter.e2e.example;

import com.slickqa.jupiter.SlickBaseTest;
import com.slickqa.jupiter.annotations.TestCaseInfo;
import com.slickqa.jupiter.annotations.Step;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCaseInfoTests extends SlickBaseTest {

    @Test
    @DisplayName("DisplayName Only Test")
    public void testDisplayName() {
    }

    @Test
    @TestCaseInfo(
            title = "TestCaseInfo Only Test",
            purpose = "Test if TestCaseInfo annotations provide data to plugin.",
            steps = {
                    @Step(step = "First Step", expectation = "With expectation"),
                    @Step(step = "Second Step")
            },
            feature = "a feature",
            component = "example component",
            author = "yomama",
            automationId = "UniqueAutomationIdForThisTest",
            automationKey = "AutomationKey"
    )
    public void testTestcaseInfo() {
    }

    @Test
    @TestCaseInfo(
            title = "TestCaseInfo and DisplayName test",
            steps = {}
    )
    @DisplayName("Junit only test name")
    public void testDisplayNameAndTestcaseInfo() {
    }

    @Test
    @TestCaseInfo()
    @DisplayName("Name from DisplayName")
    public void testDisplayNameEmptyTestcaseInfoName() {
    }

    @Test
    public void testMethodNameOnly() {
    }


    @Test
    @TestCaseInfo(
            title = "Test of Triage Note",
            steps = {},
            triageNote = "Yomama"
    )
    public void testTriageNote() {
        assertTrue(false, "This will cause a triage note to be applied");
    }

    @ParameterizedTest(name="Test {0}")
    @ValueSource(strings={"Yomama"})
    public void testTemplatedName(String parameter) {
    }

}
