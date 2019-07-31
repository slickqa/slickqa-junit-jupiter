package com.slickqa.jupiter.e2e.tests;

import com.slickqa.client.model.Result;
import com.slickqa.jupiter.DefaultAttributes;
import com.slickqa.jupiter.e2e.SlickJunitRunner;
import com.slickqa.jupiter.e2e.example.ExampleTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CustomAttributeE2ETests {
    private SlickJunitRunner util;

    @BeforeEach
    public void setup() throws Exception {
        util = new SlickJunitRunner();
    }

    @Test
    @DisplayName("When appropriate system property is added an attribute is set on the result.")
    public void testAttributeAddedToResultForSystemProperty() throws Exception {
        String attributeName = "yomama" + (new Date()).getTime();
        String attributeValue = "value" + (new Date()).getTime();
        System.setProperty("attr." + attributeName, attributeValue);
        Method test = ExampleTest.class.getMethod("examplePassTest");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        assertNotNull(result.getAttributes(), "Attributes on the result should not be null");
        assertTrue(result.getAttributes().containsKey(attributeName), "The attribute '" + attributeName + "' should be in the attributes on the result.  Available attributes are: " + Arrays.toString(result.getAttributes().keySet().toArray()));
        assertEquals(attributeValue, result.getAttributes().get(attributeName), "The value for the attribute was not properly set.");
    }
}
