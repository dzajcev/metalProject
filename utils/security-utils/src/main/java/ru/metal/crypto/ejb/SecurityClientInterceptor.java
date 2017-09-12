package ru.metal.crypto.ejb;

import org.jboss.ejb.client.EJBClientInterceptor;
import org.jboss.ejb.client.EJBClientInvocationContext;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by User on 12.09.2017.
 */
public class SecurityClientInterceptor implements EJBClientInterceptor {
    private static Logger logger = Logger.getLogger(SecurityClientInterceptor.class.getName());

    public void handleInvocation(EJBClientInvocationContext context) throws Exception {

        Map<String, Object> contextData = context.getContextData();
        if(logger.isLoggable(Level.FINEST)) {
            logger.finest("invoked proxy" + context.getInvokedProxy());
        }
        PermissionContextData permissionContextData = UserContextHolder.getPermissionContextDataThreadLocal();
        if (permissionContextData != null) {
            contextData.put(UserContextHolder.DELEGATED_USER_KEY, permissionContextData);
        }
        context.sendRequest();


    }

    public Object handleInvocationResult(EJBClientInvocationContext context) throws Exception {

        return context.getResult();
    }
}
