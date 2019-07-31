package com.slickqa.jupiter;

import com.slickqa.jupiter.DefaultAttributes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultAttributesTests {
    HashMap<String, String> environmentVariables;
    String attributeName;
    String attributeValue;

    @BeforeEach
    public void setup() {
        environmentVariables = new HashMap<>(System.getenv());
        DefaultAttributes.EnvironmentVariables = environmentVariables;
        attributeName = getUniqueAttrubuteName();
        attributeValue = getUniqueAttributeValue();
    }

    @AfterEach
    public void cleanup() {
        DefaultAttributes.EnvironmentVariables = System.getenv();
        if(System.getProperties().containsKey(attributeName)) {
            System.getProperties().remove(attributeName);
        }
    }

    public void setProperty(String name, String value) {
        System.getProperties().setProperty("attr." + name, value);
    }

    public void setEnvironmentVariable(String name, String value) {
        environmentVariables.put("ATTR_" + name, value);
    }

    public String getUniqueAttrubuteName() {
        return "yomama" + (new Date()).getTime();
    }

    public String getUniqueAttributeValue() {
        return "foo" + (new Date()).getTime();
    }

    @Test
    @DisplayName("Attribute can come from system property")
    public void testAttributeFoundFromSystemProperty() throws Exception {
        setProperty(attributeName, attributeValue);
        HashMap<String, String> attrs = DefaultAttributes.getAttributesFromEnvironment(false);
        assertTrue(attrs.containsKey(attributeName), "attributes should contain key '" + attributeName + "', but didn't.  Keys available are: " + Arrays.toString(attrs.keySet().toArray()));
        assertEquals(attributeValue, attrs.get(attributeName), "attribute value was not set correctly.");
    }

    @Test
    @DisplayName("Attribute can come from environment variable")
    public void testAttributeFoundFromEnvironmentVariable() throws Exception {
        setEnvironmentVariable(attributeName, attributeValue);
        HashMap<String, String> attrs = DefaultAttributes.getAttributesFromEnvironment(false);
        assertTrue(attrs.containsKey(attributeName), "attributes should contain key '" + attributeName + "', but didn't.  Keys available are: " + Arrays.toString(attrs.keySet().toArray()));
        assertEquals(attributeValue, attrs.get(attributeName), "attribute value was not set correctly.");
    }

    @Test
    @DisplayName("Cache returns equal but distinct ")
    public void testCacheReturnsNewHashmapSameValues() throws Exception {
        setProperty(attributeName, attributeValue);
        HashMap<String, String> attrs = DefaultAttributes.getAttributesFromEnvironment(false);
        HashMap<String, String> attrs2 = DefaultAttributes.getAttributesFromEnvironment();
        assertNotSame(attrs, attrs2, "Each instance of hashmap for attributes should be unique.");
        assertNotSame(attrs, DefaultAttributes.attributesCache, "Cache should be different instance from returned attribute map.");
        assertNotSame(attrs2, DefaultAttributes.attributesCache, "Cache should be different instance from returned attribute map.");
        assertTrue(attrs.containsKey(attributeName), "attributes should contain key '" + attributeName + "', but didn't.  Keys available are: " + Arrays.toString(attrs.keySet().toArray()));
        assertEquals(attributeValue, attrs.get(attributeName), "attribute value was not set correctly.");
        assertTrue(attrs2.containsKey(attributeName), "attributes should contain key '" + attributeName + "', but didn't.  Keys available are: " + Arrays.toString(attrs.keySet().toArray()));
        assertEquals(attributeValue, attrs2.get(attributeName), "attribute value was not set correctly.");
    }

    @Test
    @DisplayName("Cache is properly populated.")
    public void testCachePopulated() throws Exception {
        setEnvironmentVariable(attributeName, attributeValue);
        DefaultAttributes.attributesCache = null;
        HashMap<String, String> attrs = DefaultAttributes.getAttributesFromEnvironment();

        // validate returned attributes
        assertNotNull(attrs, "The return value of getAttributesFromEnvironment should never be null");
        assertTrue(attrs.containsKey(attributeName), "attributes should contain key '" + attributeName + "', but didn't.  Keys available are: " + Arrays.toString(attrs.keySet().toArray()));
        assertEquals(attributeValue, attrs.get(attributeName), "attribute value was not set correctly.");

        // validate cache
        assertNotNull(DefaultAttributes.attributesCache, "The cache of attributes should not be null");
        assertTrue(DefaultAttributes.attributesCache.containsKey(attributeName), "attributes in cache should contain key '" + attributeName + "', but didn't.  Keys available are: " + Arrays.toString(DefaultAttributes.attributesCache.keySet().toArray()));
        assertEquals(attributeValue, DefaultAttributes.attributesCache.get(attributeName), "attribute value in cache was not set correctly.");
    }

}
