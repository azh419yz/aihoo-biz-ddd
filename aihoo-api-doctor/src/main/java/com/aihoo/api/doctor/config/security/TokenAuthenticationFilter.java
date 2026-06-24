package com.aihoo.api.doctor.config.security;

import com.aihoo.util.JSONUtil;
import com.aihoo.util.StringUtil;
import com.aihoo.common.BizResult;
import com.aihoo.common.BizResultCode;
import com.aihoo.redis.RedisConstant;
import com.aihoo.redis.RedisService;
import com.aihoo.security.AuthUtil;
import com.aihoo.security.LoginUser;
import com.aihoo.domain.doctor.entity.DoctorUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import com.aihoo.redis.RedisService;

import static com.aihoo.api.doctor.config.security.PublicEndpoints.PUBLIC_URLS;

@Slf4j
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    @Autowired
    private RedisService redisService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        for (String path : PUBLIC_URLS) {
            if (uri.startsWith(path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String accessToken = request.getHeader("accessToken");

            if (StringUtil.isBlank(accessToken)) {
                sendUnauthorizedResponse(response);
                return;
            }
            String redisKey = RedisConstant.DOCKER_LOGIN_KEY + accessToken;
            if (!redisService.exists(redisKey)) {
                logger.info("没有查询到该用户缓存key:{}", redisKey);
                sendUnauthorizedResponse(response);
                return;
            }

            DoctorUser user = (DoctorUser) redisService.get(redisKey);
            if (Objects.isNull(user)) {
                sendUnauthorizedResponse(response);
                return;
            }

            redisService.expire(redisKey, RedisConstant.TOKEN_SURVIVE_TIME);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            AuthUtil.setLoginUser(toLoginUser(user));
            log.info("\n\n{}\n{}\n{}\n{}\n\n", request.getRequestURI(), user.getId(), accessToken, user);
            filterChain.doFilter(request, response);
        } finally {

            AuthUtil.clear();
        }
    }

    private LoginUser toLoginUser(DoctorUser user) {
        LoginUser loginUser = new LoginUser();
        loginUser.setId(user.getId());
        loginUser.setName(user.getName());
        loginUser.setType("DOCTOR");
        return loginUser;
    }

    private void sendUnauthorizedResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        BizResult<?> result = BizResult.fail(BizResultCode.UNAUTHORIZED);
        response.getWriter().write(JSONUtil.toJson(result));
    }
}
