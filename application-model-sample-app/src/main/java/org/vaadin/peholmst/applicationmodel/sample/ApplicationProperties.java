package org.vaadin.peholmst.applicationmodel.sample;

import java.util.Properties;

/**
 * TODO Document me!
 */
public final class ApplicationProperties {

    private static ApplicationProperties INSTANCE = new ApplicationProperties();

    private final Properties applicationProperties;

    private ApplicationProperties() {
        applicationProperties = new Properties();
        try {
            applicationProperties.load(getClass().getResourceAsStream("/application.properties"));
        } catch (Exception ex) {
            ex.printStackTrace(System.err); // In a real-world application, you
                                            // would use a logger
        }
    }

    /**
     * 
     * @param propertyName
     * @param defaultValue
     * @return
     */
    public String getProperty(String propertyName, String defaultValue) {
        return applicationProperties.getProperty(propertyName, defaultValue);
    }

    /**
     * 
     * @param propertyName
     * @return
     */
    public String getProperty(String propertyName) {
        return applicationProperties.getProperty(propertyName);
    }

    /**
     * 
     * @return
     */
    public static ApplicationProperties getInstance() {
        return INSTANCE;
    }
}
