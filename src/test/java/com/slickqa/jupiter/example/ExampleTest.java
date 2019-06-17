package com.slickqa.jupiter.example;

import com.slickqa.jupiter.SlickBaseTest;
import com.slickqa.jupiter.annotations.SlickMetaData;
import com.slickqa.jupiter.annotations.Step;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

@DisplayName("ExampleTests")
public class ExampleTest extends SlickBaseTest {


    @Test
    @DisplayName("Example pass test")
    @SlickMetaData(title = "Slick metadata title Example pass test",
            purpose = "Example test that passes",
            component = "slick-junit5",
            feature = "Example pass test feature",
            steps = {
                    @Step(step = ": assert true",
                            expectation = "appear - 'Please enter a valid phone number.'."),
            },
            author = "leeard",
            triageNote = "Example pass test triage note"
    )
    public void examplePassTest() throws Exception {
        Assertions.assertTrue(true);
    }

    @Test
    @DisplayName("Example fail test")
    public void testExampleFail() throws Exception {
        Assertions.assertTrue(false);
    }

    @Test
    public void exampleAbortedTest() throws Exception {
        int myint = 1/0;

    }

    @Test
    @DisplayName("Example disabled test")
    @Disabled
    public void exampleDisabledTest() throws Exception {
    }
}
