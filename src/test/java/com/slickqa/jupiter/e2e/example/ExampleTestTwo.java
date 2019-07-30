package com.slickqa.jupiter.e2e.example;

import com.slickqa.jupiter.SlickBaseTest;
import com.slickqa.jupiter.annotations.TestCaseInfo;
import com.slickqa.jupiter.annotations.Step;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.time.Duration.ofMillis;
import static org.junit.jupiter.api.Assertions.assertTimeout;

@DisplayName("ExampleTests")
public class ExampleTestTwo extends SlickBaseTest {


    @Test
    @DisplayName("With TestCaseInfo pass test")
    @TestCaseInfo(title = "Example pass test in test case info",
            purpose = "Example test that passes",
            component = "slick-junit5",
            feature = "Example pass test feature",
            steps = {
                    @Step(step = "simply assert true",
                            expectation = "test should pass"),
            },
            author = "leeard",
            triageNote = "pass test triage note"
    )

    public void testNoTestCaseInfoNoDisplayName() throws Exception {
        Assertions.assertTrue(true);
    }

    @Test
    @TestCaseInfo(title = "Fail with TestCaseInfo and DisplayName",
            purpose = "Slick test info takes precedence",
            component = "slick-junit5",
            feature = "Testwatcher",
            steps = {
                    @Step(step = "simply assert true",
                            expectation = "test should pass"),
            },
            author = "leeard",
            triageNote = "pass test triage note"
    )
    @DisplayName("This title should not show in slick")
    public void testBothTestCaseInfoAndDisplayName() throws Exception {
        Assertions.assertTrue(true);
    }

    @Test
    @DisplayName("Fail test with junit 5 Display Name only")
    public void testExampleFail() throws Exception {
        Assertions.assertTrue(false);
    }

    @Test
    public void shouldBeBrokenTest() throws Exception {
        int myint = 1/0;

    }

    @Test
    @DisplayName("Example disabled test two")
    @Disabled
    public void exampleDisabledTest() throws Exception {
    }

    @Test
    public void examplePassNoTestCaseInfoOrDisplayNameTest() throws Exception {
    }

    @Test
    void assertTimeout_runsLate_failsButFinishes() {
        assertTimeout(ofMillis(100), () -> {
            Thread.sleep(200);
            // you will see this message
            System.out.println("Woke up");
        });
    }

    @TestCaseInfo(
            title = "Simple example of a passing test",
            steps = {
            }
    )
    @Test
    public void examplePassTest() {
    }

}

