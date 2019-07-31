package com.slickqa.jupiter;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DefaultAttributes {
    protected static HashMap<String, String> attributesCache = null;

    protected static Properties SystemProperties = System.getProperties();
    protected static Map<String, String> EnvironmentVariables = System.getenv();


    public static HashMap<String, String> getAttributesFromEnvironment(boolean useCache) {
        if(useCache && attributesCache != null) {
            return new HashMap<>(attributesCache);
        }
        HashMap<String, String> attrs = new HashMap<>();
        for(String name : SystemProperties.stringPropertyNames()) {
            if(name.startsWith("attr.")) {
                attrs.put(name.substring(5), SystemProperties.getProperty(name));
            }
        }
        for(String name : EnvironmentVariables.keySet()) {
            if(name.startsWith("ATTR_")) {
                attrs.put(name.substring(5), EnvironmentVariables.get(name));
            }
        }
        attributesCache = new HashMap<>(attrs);
        return attrs;
    }

    public static HashMap<String, String> getAttributesFromEnvironment() {
        return getAttributesFromEnvironment(true);
    }
}
