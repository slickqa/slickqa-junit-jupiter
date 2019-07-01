package com.slickqa.jupiter.e2e.example;

import com.slickqa.jupiter.SlickBaseTest;
import com.slickqa.jupiter.annotations.SlickMetaData;
import com.slickqa.jupiter.annotations.Step;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MetaDataTests extends SlickBaseTest {

    @Test
    @DisplayName("DisplayName Only Test")
    public void testDisplayName() {
    }

    @Test
    @SlickMetaData(
            title = "SlickMetaData Only Test",
            purpose = "Test if SlickMetaData annotations provide data to plugin.",
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
    public void testSlickMetaData() {
    }

    @Test
    @SlickMetaData(
            title = "SlickMetaData and DisplayName test",
            steps = {}
    )
    @DisplayName("Junit only test name")
    public void testDisplayNameAndSlickMetaData() {
    }

    @Test
    public void testMethodNameOnly() {
    }


    @Test
    @SlickMetaData(
            title = "Test of Triage Note",
            steps = {},
            triageNote = "Yomama"
    )
    public void triageNoteTest() {
        assertTrue(false, "This will cause a triage note to be applied");
    }

}
