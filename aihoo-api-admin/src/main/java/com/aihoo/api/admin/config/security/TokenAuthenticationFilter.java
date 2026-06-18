package com.aihoo.api.admin.config.security;

import static com.aihoo.api.admin.config.security.PublicEndpoints.PUBLIC_URLS;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.aihoo.domain.sys.entity.SysUser;
import com.aihoo.domain.sys.security.SysUserDetails;
import com.aihoo.util.JSONUtil;
import com.aihoo.util.StringUtil;
import com.aihoo.common.BizResult;
import com.aihoo.common.BizResultCode;
import com.aihoo.redis.RedisConstant;
import com.aihoo.redis.RedisService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Token based authentication filter.
 */
@Slf4j
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private RedisService redisService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        for (String path : PUBLIC_URLS) {
            if (path.endsWith("/**")) {
                // 处理通配符路径，如 /api/v1/file/**
                String prefix = path.substring(0, path.length() - 3);
                if (uri.startsWith(prefix)) {
                    return true;
                }
            } else {
                // 处理精确路径
                if (uri.equals(path)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String accessToken = getAccessTokenFromRequest(request);

            if (StringUtil.isBlank(accessToken)) {
                sendUnauthorizedResponse(response);
                return;
            }
            String redisKey = RedisConstant.ADMIN_LOGIN_KEY + accessToken;
            if (!redisService.exists(redisKey)) {
                sendUnauthorizedResponse(response);
                return;
            }

            SysUser user = (SysUser) redisService.get(redisKey);
            if (Objects.isNull(user)) {
                sendUnauthorizedResponse(response);
                return;
            }

            // 刷新 token 过期时间
            redisService.expire(redisKey, RedisConstant.SESSION_SURVIVE_TIME);

            // 👇 关键：将用户信息存入 Spring Security 上下文
            SysUserDetails loginUser = new SysUserDetails(user, Collections.emptySet());
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginUser, null, Collections.emptyList());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.info("\n\n{}\n{}\n{}\n{}\n\n", request.getRequestURI(), user.getId(), accessToken, user);
            filterChain.doFilter(request, response);
        } finally {
            // 清理ThreadLocal（如果需要的话）
        }
    }

    private void sendUnauthorizedResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK); // 保持 200，与原逻辑一致
        response.setContentType("application/json;charset=UTF-8");
        BizResult<?> result = BizResult.fail(BizResultCode.UNAUTHORIZED);
        response.getWriter().write(JSONUtil.toJson(result));
    }
    
    
	private String getAccessTokenFromRequest(HttpServletRequest request) {
		String heouToken = request.getHeader("heou-token");
		if (StringUtils.hasText(heouToken) && heouToken.startsWith("heou-")) {
			return heouToken.substring(5);
		}

		// 也可以从Cookie中获取token
		String tokenCookie = null;
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if ("heou-token".equals(cookie.getName())) {
					tokenCookie = cookie.getValue();
					break;
				}
			}
		}

		return tokenCookie;
	}
    
    
    
}
