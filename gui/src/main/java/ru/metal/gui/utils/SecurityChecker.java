package ru.metal.gui.utils;

import ru.metal.security.ejb.dto.Privilege;
import ru.metal.security.ejb.dto.Role;
import ru.metal.security.ejb.security.SecurityUtils;

/**
 * Created by User on 18.09.2017.
 */
public class SecurityChecker {
    public static boolean checkRole(Role role, Privilege privilege) {
        if (SecurityUtils.checkPrivileges(privilege) &&
                SecurityUtils.checkUserRoles(role)) {
            return true;
        }
        return false;
    }

    public static boolean checkRole(Role role) {
        if (SecurityUtils.checkUserRoles(role)) {
            return true;
        }
        return false;
    }

    public static boolean checkRole(Privilege privilege) {
        if (SecurityUtils.checkPrivileges(privilege)) {
            return true;
        }
        return false;
    }
}
