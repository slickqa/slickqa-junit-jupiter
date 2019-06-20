package com.slickqa.jupiter;

public class PropertyOrEnvVariableConfigurationSource implements SlickConfigurationSource {

    @Override
    public String getConfigurationEntry(String name) {
        String retval = System.getProperty(name);
        if(retval == null || retval.isEmpty()){
            retval = System.getenv(name);
            if (retval == null) {
                retval = System.getenv(name.toUpperCase().replaceAll("\\.", "_"));
            }
        }
        return retval;
    }

    @Override
    public String getConfigurationEntry(String name, String defaultValue) {
        String retval = System.getProperty(name);
        if(retval == null || retval.isEmpty()){
            retval = System.getenv(name);
            if (retval == null) {
                retval = System.getenv(name.toUpperCase().replaceAll("\\.", "_"));
            }
        }
        if (retval == null) {
            return defaultValue;
        }
        return retval;
    }
}
