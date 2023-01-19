package com.akehcloud.iecube.util;

import com.akehcloud.exception.runtime.AuthException;
import com.akehcloud.exception.runtime.SystemException;
import com.akehcloud.iecube.module.auth.dto.CurrentUserDTO;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 网络层工具类
 *
 * @author panghaoyue
 */
@Slf4j
public class AuthUtils {

    public static final String ACCESS_TOKE_KEY = "x-access-token";
    public static final String APP_CODE_SECRET = "app-code";

    private static final String SECRET = "aksjdflajs";
    private static final String USER_REDIS_KEY_PIX = "USER_";
    private static final String USER_TOKEN_REDIS_KEY_PIX = "USER_TOKEN_";

    private static final ThreadLocal<CurrentUserDTO> LOCAL_USER = new ThreadLocal<>();
    private static final ThreadLocal<String> LOCAL_APP = new ThreadLocal<>();


    private AuthUtils() {
    }

    public static String createToken(Long userId) {
        return JwtUtils.sign(userId, SECRET);
    }

    public static void rm(StringRedisTemplate redisTemplate) {
        redisTemplate.delete(getUserRedisKey(getCurrentUserId()));
        redisTemplate.delete(getUserTokenRedisKey(getCurrentUserId()));
    }

    public static void cache(CurrentUserDTO user, String token, StringRedisTemplate redisTemplate) {
        try {
            redisTemplate.opsForValue().set(
                    getUserRedisKey(user.getId()),
                    new ObjectMapper().writeValueAsString(user), 30, TimeUnit.MINUTES);
        } catch (JsonProcessingException e) {
            log.error("转JSON失败", e);
            throw new SystemException();
        }
        redisTemplate.opsForValue().set(
                getUserTokenRedisKey(user.getId()),
                token, 30, TimeUnit.MINUTES);
    }

    public static void setCurrentUser(Long id, StringRedisTemplate redisTemplate) {
        CurrentUserDTO user = getAuthInfo(id, redisTemplate);
        LOCAL_USER.set(user);
    }

    public static CurrentUserDTO getAuthInfo(Long id, StringRedisTemplate redisTemplate) {
        String userJson = redisTemplate.opsForValue().get(getUserRedisKey(id));
        try {
            return new ObjectMapper().readValue(userJson, CurrentUserDTO.class);
        } catch (IOException e) {
            log.error("解析JSON失败", e);
            throw new AuthException();
        }
    }

    public static boolean authed(String token, StringRedisTemplate redisTemplate) {
        Long id;
        try {
            id = JwtUtils.parse(token, SECRET, Long.class);
        } catch (TokenExpiredException | SignatureVerificationException e) {
            throw new AuthException("登录失效，请重新登录");
        }
        String t = redisTemplate.opsForValue().get(getUserTokenRedisKey(id));
        if (t != null && t.equals(token)) {
            setCurrentUser(id, redisTemplate);
            flushExpireTime(redisTemplate);
            return true;
        }
        return false;
    }

    public static void flushExpireTime(StringRedisTemplate redisTemplate) {
        redisTemplate.expire(getUserRedisKey(getCurrentUserId()), 30, TimeUnit.MINUTES);
        redisTemplate.expire(getUserTokenRedisKey(getCurrentUserId()), 30, TimeUnit.MINUTES);
    }

    public static CurrentUserDTO getCurrentUser() {
        CurrentUserDTO userDTO = LOCAL_USER.get();
        if (userDTO == null) {
            throw new AuthException();
        }
        return userDTO;
    }

    public static String getCurrentUserEmail() {
        return getCurrentUser().getEmail();
    }

    public static Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public static Long getCurrentUserSchoolId() {
        return getCurrentUser().getSchoolId();
    }

    public static Long getCurrentUserOrganizationId() {
        return getCurrentUser().getOrganizationId();
    }

    private static String getUserRedisKey(Long userId) {
        return (USER_REDIS_KEY_PIX + userId).toUpperCase();
    }

    private static String getUserTokenRedisKey(Long userId) {
        return (USER_TOKEN_REDIS_KEY_PIX + userId).toUpperCase();
    }

}
