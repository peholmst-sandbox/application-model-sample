package org.vaadin.peholmst.applicationmodel.sample;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.vaadin.peholmst.applicationmodel.framework.eventbus.EventBus;
import org.vaadin.peholmst.applicationmodel.sample.domain.TicketRepository;
import org.vaadin.peholmst.applicationmodel.sample.domain.TicketTypeRepository;
import org.vaadin.peholmst.applicationmodel.sample.domain.gis.GeoService;

import com.google.maps.GeoApiContext;
import org.vaadin.peholmst.applicationmodel.sample.ui.model.TicketUpdatedEventBridge;

/**
 * TODO Document me!
 */
@WebListener
public class ApplicationServices implements ServletContextListener {

    private static ApplicationServices INSTANCE;

    private GeoApiContext geoApiContext;
    private GeoService geoService;
    private TicketTypeRepository ticketTypeRepository;
    private TicketRepository ticketRepository;
    private ExecutorService executorService;
    private EventBus eventBus;
    private TicketUpdatedEventBridge ticketUpdatedEventBridge;

    public GeoApiContext getGeoApiContext() {
        return geoApiContext;
    }

    public GeoService getGeoService() {
        return geoService;
    }

    public TicketTypeRepository getTicketTypeRepository() {
        return ticketTypeRepository;
    }

    public TicketRepository getTicketRepository() {
        return ticketRepository;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        executorService = Executors.newCachedThreadPool();
        geoApiContext = new GeoApiContext()
                .setApiKey(ApplicationProperties.getInstance().getProperty("google.api-key"));
        geoService = new GeoService(geoApiContext,
                ApplicationProperties.getInstance().getProperty("google.country", "fi"));
        ticketTypeRepository = new TicketTypeRepository();
        ticketRepository = new TicketRepository();
        eventBus = new EventBus(executorService);
        ticketUpdatedEventBridge = new TicketUpdatedEventBridge(eventBus);
        INSTANCE = this;
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        executorService.shutdown();
        INSTANCE = null;
    }

    public static ApplicationServices getInstance() {
        return INSTANCE;
    }
}
