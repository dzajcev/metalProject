package ru.metal.security.ejb.security;

import ru.metal.security.ejb.AccessCheckException;
import ru.metal.security.ejb.PermissionContextData;
import ru.metal.security.ejb.UserContextHolder;
import ru.metal.security.ejb.dto.Privilege;
import ru.metal.security.ejb.dto.Role;
import ru.metal.security.ejb.security.checker.SecurityChecker;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.lang.reflect.Method;

/**
 * Created by vpredtechenskaya on 10/22/2015.
 */
public class SecurityUtils {

    public static String getUserGUID() {
        final PermissionContextData contextData = getContextData();
        return contextData.getUserGuid();
    }

    public static boolean isSystemUser() {
        final PermissionContextData contextData = getContextData();
        return contextData.isSystemUser();
    }


    public static boolean checkPrivileges(final Privilege... expectedPrivileges) {
        if (expectedPrivileges.length==0){
            return true;
        }
        final PermissionContextData contextData = getContextData();
        if (contextData.getPrivileges() != null) {
            for (final Privilege privilege : expectedPrivileges) {
                if (!contextData.getPrivileges().contains(privilege)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean checkUserRoles(final Role... expectedRoles) {
        if (expectedRoles.length==0){
            return true;
        }
        final PermissionContextData contextData = getContextData();
        if (contextData.getRoles() != null) {
            for (final Role role : expectedRoles) {
                if (!contextData.getRoles().contains(role)) {
                    return false;
                }
            }
        }

        return true;
    }

    private static PermissionContextData getContextData() {
        final PermissionContextData contextData = UserContextHolder.getPermissionContextDataThreadLocal();
        if (contextData == null) {
            throw new AccessCheckException("PermissionContextData not set");
        }
        return contextData;
    }

    private static SecurityChecker lookupChecker(final String name) throws NamingException {
        return (SecurityChecker) InitialContext.doLookup("java:module/" + name);
    }


}
