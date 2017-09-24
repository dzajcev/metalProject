package ru.metal.gui.utils;

import ru.metal.security.ejb.dto.Privilege;
import ru.metal.security.ejb.dto.Role;
import ru.metal.security.ejb.security.SecurityUtils;

/**
 * Created by User on 18.09.2017.
 */
public class SecurityChecker {
    public static boolean checkRoles(Role[] roles, Privilege[] privilege) {
        if (SecurityUtils.checkPrivileges(privilege) &&
                SecurityUtils.checkUserRoles(roles)) {
            return true;
        }
        return false;
    }

    public static boolean checkRoles(Role... roles) {
        if (SecurityUtils.checkUserRoles(roles)) {
            return true;
        }
        return false;
    }

    public static boolean checkRoles(Privilege... privileges) {
        if (SecurityUtils.checkPrivileges(privileges)) {
            return true;
        }
        return false;
    }
}
