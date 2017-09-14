package ru.metal.crypto.ejb;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.util.logging.Logger;


public class EjbSecurityServerInterceptor {
    private static Logger logger = Logger.getLogger(EjbSecurityServerInterceptor.class.getName());

    @AroundInvoke
    public Object setContextUser(InvocationContext context) throws Exception {
        if (context.getContextData().get(UserContextHolder.DELEGATED_USER_KEY) != null) {
            PermissionContextData permissionContextData = PermissionContextData.deserialize((byte[]) context.getContextData().get(UserContextHolder.DELEGATED_USER_KEY));
            if (permissionContextData != null) {
                UserContextHolder.setUserPermissionDataThreadLocal(permissionContextData);
            }
        } else {
            logger.info("Security context is not set. ThreadLocal<PermissionContextData> will be null");
        }
        return context.proceed();
    }
}
