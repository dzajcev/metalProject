package ru.metal.security.ejb.security;

import ru.metal.security.ejb.AccessCheckException;
import ru.metal.security.ejb.PermissionContextData;
import ru.metal.security.ejb.UserContextHolder;
import ru.metal.security.ejb.dto.Privilege;
import ru.metal.security.ejb.dto.Role;
import ru.metal.security.ejb.security.checker.SecurityChecker;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by vpredtechenskaya on 10/22/2015.
 */
public class SecurityUtils {

    public static String getUserGUID() {
        final PermissionContextData contextData = getContextData();
        return contextData.getUser().getGuid();
    }

    public static boolean isSystemUser() {
        final PermissionContextData contextData = getContextData();
        return contextData.isSystemUser();
    }


    public static boolean checkPrivileges(final Privilege... expectedPrivileges) {
        if (expectedPrivileges.length == 0) {
            return true;
        }
        final PermissionContextData contextData = getContextData();

        List<Privilege> privileges = new ArrayList<>();
        privileges.addAll(Arrays.asList(expectedPrivileges));
        for (DelegatingUser delegatingUser : contextData.getUser().getDonorRights()) {
            privileges.addAll(delegatingUser.getPrivileges());
        }
        if (contextData.getUser().getPrivileges() != null) {
            for (final Privilege privilege : expectedPrivileges) {
                if (!privileges.contains(privilege)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean checkUserRoles(final Role... expectedRoles) {
        if (expectedRoles.length == 0) {
            return true;
        }
        final PermissionContextData contextData = getContextData();
        List<Role> roles = new ArrayList<>();
        roles.addAll(Arrays.asList(expectedRoles));
        for (DelegatingUser delegatingUser : contextData.getUser().getDonorRights()) {
            roles.addAll(delegatingUser.getRoles());
        }
        if (contextData.getUser().getRoles() != null) {
            for (final Role role : expectedRoles) {
                if (!contextData.getUser().getRoles().contains(role)) {
                    return false;
                }
            }
        }

        return true;
    }


    public static List<String> getAllUsersGuids() {
        PermissionContextData contextData = getContextData();
        List<String> result = new ArrayList<>();
        result.add(contextData.getUser().getGuid());
        for (DelegatingUser delegatingUser : contextData.getUser().getDonorRights()) {
            result.add(delegatingUser.getUserGuid());
        }
        return result;
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
