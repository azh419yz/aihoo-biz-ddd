package com.aihoo.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    
    public static String getLoginUserId() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication.getPrincipal() == null) {
                return null;
            }
            Object principal = authentication.getPrincipal();
            return extractId(principal);
        } catch (Exception e) {
            return null;
        }
    }

    
    private static String extractId(Object principal) {
        if (principal == null) {
            return null;
        }
        try {
            Object sysUser = principal.getClass().getMethod("getSysUser").invoke(principal);
            if (sysUser != null) {
                Object id = sysUser.getClass().getMethod("getId").invoke(sysUser);
                return id == null ? null : id.toString();
            }
        } catch (NoSuchMethodException ignore) {

        } catch (Exception e) {
            return null;
        }
        try {
            Object id = principal.getClass().getMethod("getId").invoke(principal);
            return id == null ? null : id.toString();
        } catch (Exception e) {
            return null;
        }
    }

    
    public static String encryptPassword(String password) {
        return new Md5PasswordEncoder().encode(password);
    }

    
    public static Object getLoginUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication.getPrincipal() == null) {
                return null;
            }
            Object principal = authentication.getPrincipal();

            try {
                return principal.getClass().getMethod("getSysUser").invoke(principal);
            } catch (NoSuchMethodException ignore) {
                return principal;
            }
        } catch (Exception e) {
            return null;
        }
    }

    
    public static String extractPassword(Object principal) {
        if (principal == null) {
            return null;
        }
        try {
            Object p = principal.getClass().getMethod("getPassword").invoke(principal);
            return p == null ? null : p.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
