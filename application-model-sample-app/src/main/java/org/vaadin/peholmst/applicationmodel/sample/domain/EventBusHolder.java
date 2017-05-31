package org.vaadin.peholmst.applicationmodel.sample.domain;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.vaadin.peholmst.applicationmodel.framework.eventbus.EventBus;

/**
 * TODO Document me
 */
@WebListener
public class EventBusHolder implements ServletContextListener {

    private ExecutorService executorService;
    private static EventBus INSTANCE;

    /**
     * 
     * @return
     */
    public static EventBus getInstance() {
        return INSTANCE;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        executorService = Executors.newCachedThreadPool();
        INSTANCE = new EventBus(executorService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        INSTANCE = null;
        executorService.shutdown();
    }
}
