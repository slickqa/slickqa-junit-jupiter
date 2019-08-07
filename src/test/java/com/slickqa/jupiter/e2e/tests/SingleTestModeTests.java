package com.slickqa.jupiter.e2e.tests;

import com.slickqa.client.model.Result;
import com.slickqa.jupiter.ConfigurationNames;
import com.slickqa.jupiter.SlickBaseTest;
import com.slickqa.jupiter.SlickJunitController;
import com.slickqa.jupiter.SlickJunitControllerFactory;
import com.slickqa.jupiter.annotations.Step;
import com.slickqa.jupiter.annotations.TestCaseInfo;
import com.slickqa.jupiter.e2e.SlickJunitRunner;
import com.slickqa.jupiter.e2e.example.ExampleTest;
import org.junit.jupiter.api.*;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;


@DisplayName("ExampleTests")
public class SingleTestModeTests extends SlickBaseTest {
    private SlickJunitRunner util;
    private Result existingResult;
    private String resultURL;

    @BeforeEach
    public void setUp() throws Exception{
        util = new SlickJunitRunner();
        Method test = ExampleTest.class.getMethod("examplePassTest");
        existingResult = util.runTestMethod(test);
        if(existingResult == null) {
            throw new Exception("Setup error.  Tried to create a result to use for an existing result and failed");
        }
        resultURL = util.getSlickBaseURL() + "/results/" + existingResult.getId();


    }

    @Test
    @TestCaseInfo(title = "Single test runs with matching result URL",
            purpose = "Single test mode will run a test if slick.resulturl matches the test to be run",
            author = "Lee Higginson"
    )
    public void singleTestModeMatchingResultURL() throws Exception {
        Method test = ExampleTest.class.getMethod("examplePassTest");
        SlickJunitControllerFactory.INSTANCE = null;
        SlickJunitController.currentResult = null;
        util.setResultUrl(resultURL);
        Result result = util.runTestMethod(test, true);
        assertNotNull(result, "The result from slick should not be null");
        assertEquals(existingResult.getId(), result.getId(), "Result ID should be the same as the result passed in");
    }

    @AfterEach
    public void cleanUp() {
        System.setProperty(ConfigurationNames.RESULT_URL, "");
        util.setResultUrl("");
    }

}

