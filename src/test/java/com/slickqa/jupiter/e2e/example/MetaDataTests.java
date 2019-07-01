package com.slickqa.jupiter.e2e.example;

import com.slickqa.jupiter.annotations.SlickMetaData;
import com.slickqa.jupiter.annotations.Step;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MetaDataTests {

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
            author = "yomama"
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

}
