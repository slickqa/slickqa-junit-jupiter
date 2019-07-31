package com.slickqa.jupiter.e2e.example;

import com.slickqa.jupiter.SlickBaseTest;
import com.slickqa.jupiter.annotations.TestCaseInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DataDrivenTests extends SlickBaseTest {
    @TestCaseInfo(
            purpose = "To verify that plain parameterized tests from JUnit can be used in slick.",
            component = "SlickJunitController",
            feature = "DataDriven"
    )
    @ParameterizedTest(name = "Simple parameterized test {0}")
    @ValueSource(strings = {"one" , "two", "three"})
    public void testSimpleParameterizedTest(String param) {
        Set<String> possibleParameters = new HashSet<>();
        possibleParameters.add("one");
        possibleParameters.add("two");
        possibleParameters.add("three");
        assertNotNull(param, "The parameter should not be null.");
        assertTrue(possibleParameters.contains(param), "The parameter \"" + param + "\" should be one of \"one\", \"two\", or \"three\".");
    }

    @TestCaseInfo(
            purpose = "Verify that custom annotation produces checksum of arguments",
            component = "SlickJunitController",
            feature = "DataDriven"
    )
    @ValueSource(strings = {"one", "two", "three"})
    @com.slickqa.jupiter.annotations.ParameterizedTest(name = "More complex parameterized test {0}")
    public void testCustomParameterizedTest(String param) {
        Set<String> possibleParameters = new HashSet<>();
        possibleParameters.add("one");
        possibleParameters.add("two");
        possibleParameters.add("three");
        assertNotNull(param, "The parameter should not be null.");
        assertTrue(possibleParameters.contains(param), "The parameter \"" + param + "\" should be one of \"one\", \"two\", or \"three\".");
    }

}
