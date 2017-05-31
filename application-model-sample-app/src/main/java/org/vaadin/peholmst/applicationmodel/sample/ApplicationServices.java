package org.vaadin.peholmst.applicationmodel.sample;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.vaadin.peholmst.applicationmodel.sample.domain.gis.GeoService;

import com.google.maps.GeoApiContext;

/**
 * TODO Document me!
 */
@WebListener
public class ApplicationServices implements ServletContextListener {

    private static ApplicationServices INSTANCE;

    private GeoApiContext geoApiContext;
    private GeoService geoService;

    public GeoApiContext getGeoApiContext() {
        return geoApiContext;
    }

    public GeoService getGeoService() {
        return geoService;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        geoApiContext = new GeoApiContext()
                .setApiKey(ApplicationProperties.getInstance().getProperty("google.api-key"));
        geoService = new GeoService(geoApiContext,
                ApplicationProperties.getInstance().getProperty("google.country", "fi"));
        INSTANCE = this;
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        INSTANCE = null;
    }

    public static ApplicationServices getInstance() {
        return INSTANCE;
    }
}
