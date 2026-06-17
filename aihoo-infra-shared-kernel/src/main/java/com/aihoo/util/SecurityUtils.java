package com.aihoo.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Security Utilities — 共享版本，泛型 API 避免对具体实体（SysUser/LoginUser）的硬依赖。
 *
 * <p>原 admin 的 com.aihoo.admin.common.security.SecurityUtils 直接返回 SysUser，
 * 紧耦合 domain-sys。本版本只返回 String userId，调用方按需 cast principal。</p>
 */
public class SecurityUtils {

    /**
     * 获取当前登录用户的 ID（String 类型，匹配各域实体的 String id）。
     * 如果 principal 有 getSysUser().getId()（如 LoginUser），使用之；
     * 否则尝试直接调用 principal.getId()。
     *
     * @return userId，未登录或 principal 不合规返回 null
     */
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

    /**
     * 通过反射提取 id：先试 getSysUser().getId()（LoginUser 模式），再试 principal.getId()。
     */
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
            // principal has no getSysUser; try getId directly
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

    /**
     * Encrypt password using default MD5 settings.
     */
    public static String encryptPassword(String password) {
        return new Md5PasswordEncoder().encode(password);
    }

    /**
     * 获取当前登录用户对象（通过反射从 principal 提取，类型由调用方 cast）。
     * 原 admin 的 SecurityUtils.getLoginUser() 返回 SysUser，本版本返回 Object。
     * <p>阶段 4 整改：将各域 service 改为通过领域 service 获取登录用户，移除此方法。</p>
     */
    public static Object getLoginUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication.getPrincipal() == null) {
                return null;
            }
            Object principal = authentication.getPrincipal();
            // LoginUser 模式：principal.getSysUser() 返回 domain-sys 的 SysUser
            try {
                return principal.getClass().getMethod("getSysUser").invoke(principal);
            } catch (NoSuchMethodException ignore) {
                return principal;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从 principal（LoginUser 包装或直接 user 对象）提取 password 字段。
     * 通过反射 getPassword()，避免对具体域实体的硬依赖。
     */
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
