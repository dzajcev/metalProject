package ru.metal.crypto.ejb;

import org.jboss.ejb.client.EJBClientContext;
import org.jboss.ejb.client.EJBClientInterceptor;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.logging.Logger;

/**
 * Created by User on 12.09.2017.
 */
public class PropagationInitializerListener implements ServletContextListener {

    private static Logger logger = Logger.getLogger(PropagationInitializerListener.class.getName());
    private EJBClientInterceptor.Registration registration;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        registration = EJBClientContext.getCurrent().registerInterceptor(10000000, new SecurityClientInterceptor());
        logger.info("WEB registered SecurityClientInterceptor");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        registration.remove();
    }
}