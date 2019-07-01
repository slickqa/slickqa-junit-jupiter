package com.slickqa.jupiter.e2e.tests;

import com.slickqa.client.model.Result;
import com.slickqa.jupiter.annotations.SlickMetaData;
import com.slickqa.jupiter.e2e.SlickJunitRunner;
import com.slickqa.jupiter.e2e.example.MetaDataTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MetaDataAccuracyTests {
    private SlickJunitRunner util;

    @BeforeEach
    public void setup() throws Exception {
        util = new SlickJunitRunner();
    }


    @Test
    @DisplayName("Test name comes from SlickMetaData annotation")
    public void testNameComesFromSlickMetaData() throws Exception {
        Method test = MetaDataTests.class.getMethod("testSlickMetaData");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        SlickMetaData info = test.getDeclaredAnnotation(SlickMetaData.class);
        assertEquals(info.title(), result.getTestcase().getName(), "name of test should come from SlickMetaData");
    }

    @Test
    @DisplayName("Test name can come from DisplayName annotation")
    public void testNameCanComeFromDisplayName() throws Exception {
        Method test = MetaDataTests.class.getMethod("testDisplayName");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        DisplayName name = test.getDeclaredAnnotation(DisplayName.class);
        assertEquals(name.value(), result.getTestcase().getName(), "name of test should come from DisplayName");
    }

    @Test
    @DisplayName("Test name comes from SlickMetaData when both SlickMetaData and DisplayName are present")
    public void testNameComesFromSlickMetaDataWhenBothArePresent() throws Exception {
        Method test = MetaDataTests.class.getMethod("testDisplayNameAndSlickMetaData");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        SlickMetaData info = test.getDeclaredAnnotation(SlickMetaData.class);
        assertEquals(info.title(), result.getTestcase().getName(), "name of test should come from SlickMetaData");
    }

    @Test
    @DisplayName("Test name comes from method name when no annotation is present")
    public void testNameComesFromMethodNameWhenNoAnnotationIsPresent() throws Exception {
        Method test = MetaDataTests.class.getMethod("testMethodNameOnly");
        Result result = util.runTestMethod(test);
        assertNotNull(result, "The result from slick should not be null");
        assertEquals(test.getName(), result.getTestcase().getName(), "name of test should come from the method name");
    }
}
