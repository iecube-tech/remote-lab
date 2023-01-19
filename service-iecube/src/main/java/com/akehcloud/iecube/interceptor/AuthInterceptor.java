package com.akehcloud.iecube.interceptor;

import com.akehcloud.exception.runtime.AuthException;
import com.akehcloud.exception.runtime.UnLoginException;
import com.akehcloud.iecube.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 认证拦截器
 *
 * @author panghaoyue
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final StringRedisTemplate redisTemplate;

    @Autowired
    public AuthInterceptor(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals(HttpMethod.OPTIONS.name())) {
            return true;
        }
        String token = request.getHeader(AuthUtils.ACCESS_TOKE_KEY);
        if (!StringUtils.hasText(token)) {
            throw new UnLoginException("请先登录");
        }
        if (AuthUtils.authed(token, redisTemplate)) {
            return true;
        }
        throw new AuthException("登录失效，请重新登录");
    }

}
