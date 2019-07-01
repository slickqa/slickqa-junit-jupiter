package com.slickqa.jupiter.e2e.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MetaDataAccuracyTests {

    @Test
    @DisplayName("Test name comes from SlickMetaData annotation")
    public void testNameComesFromSlickMetaData() throws Exception {
    }

    @Test
    @DisplayName("Test name can come from DisplayName annotation")
    public void testNameCanComeFromDisplayName() throws Exception {
    }

    @Test
    @DisplayName("Test name comes from SlickMetaData when both SlickMetaData and DisplayName are present")
    public void testNameComesFromSlickMetaDataWhenBothArePresent() throws Exception {
    }

    @Test
    @DisplayName("Test name comes from method name when no annotation is present")
    public void testNameComesFromMethodNameWhenNoAnnotationIsPresent() throws Exception {
    }
}
