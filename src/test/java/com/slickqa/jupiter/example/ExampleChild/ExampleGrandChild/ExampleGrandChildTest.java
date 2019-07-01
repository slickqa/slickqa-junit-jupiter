package com.slickqa.jupiter.example.ExampleChild.ExampleGrandChild;

import com.slickqa.jupiter.SlickBaseTest;
import com.slickqa.jupiter.annotations.SlickMetaData;
import com.slickqa.jupiter.annotations.Step;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.time.Duration.ofMillis;
import static org.junit.jupiter.api.Assertions.assertTimeout;

@DisplayName("ExampleTests")
public class ExampleGrandChildTest extends SlickBaseTest {


    @Test
    @DisplayName("With SlickMetaData pass test")
    @SlickMetaData(title = "Example pass test in metadata",
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

    public void testNoSlickMetaDataNoDisplayName() throws Exception {
        Assertions.assertTrue(true);
    }

    @Test
    @SlickMetaData(title = "Fail with SlickMetaData and DisplayName",
            purpose = "Slick metadata takes precedence",
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
    public void testBothSlickMetaDataAndDisplayName() throws Exception {
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
    @DisplayName("Example disabled test")
    @Disabled
    public void exampleDisabledTest() throws Exception {
    }

    @Test
    public void examplePassNoSlickMetaDataOrDisplayNameTest() throws Exception {
    }

    @Test
    void assertTimeout_runsLate_failsButFinishes() {
        assertTimeout(ofMillis(100), () -> {
            Thread.sleep(200);
            // you will see this message
            System.out.println("Woke up");
        });
    }

    @SlickMetaData(
            title = "Simple example of a passing test",
            steps = {
            }
    )
    @Test
    public void examplePassTest() {
    }

}

