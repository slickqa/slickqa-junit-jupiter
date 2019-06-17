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
    @SlickMetaData(title = "Phone Number input validation",
            purpose = "Verify that phone number input does not accept invalid entries",
            component = "invest",
            feature = "Customer information inputs",
            steps = {
                    @Step(step = "1: Run Invest Flow using BROKERAGE mode till Customer Information page"),
                    @Step(step = "2: Verify that the phone number input does not accept blank input.",
                            expectation = "An error message should appear - 'Please enter a valid phone number.'."),
                    @Step(step = "3: Verify that the phone number input does not accept special characters (except " +
                            "for a single '(') and any input that is no a digit will be converted into a single '('.",
                            expectation = "An error message should appear - 'Please enter a valid phone number.'."),
                    @Step(step = "4: Verify that the phone number input does not accept more than 10 digits."),
                    @Step(step = "5: Verify that the phone number input does not accept less than 10 digits.",
                            expectation = "An error message should appear - 'Phone number is invalid.'."),
            },
            author = "crperez",
            triageNote = "Whole on boarding UI for brokerage flow has been changed to react and need to be reformatted"
    )
    public void examplePassTest() throws Exception {
        System.out.println("Soooo Faaat");
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
