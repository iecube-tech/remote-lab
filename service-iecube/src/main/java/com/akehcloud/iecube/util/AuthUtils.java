package com.akehcloud.iecube.util;

import com.akehcloud.exception.runtime.AuthException;
import com.akehcloud.exception.runtime.SystemException;
import com.akehcloud.iecube.module.auth.dto.CurrentUserDTO;
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
    private static final String USER_REDIS_KEY_PIX = "username";

    private static final ThreadLocal<CurrentUserDTO> LOCAL_USER = new ThreadLocal<>();
    private static final ThreadLocal<String> LOCAL_APP = new ThreadLocal<>();


    private AuthUtils() {
    }

    public static void rm(StringRedisTemplate redisTemplate) {
        redisTemplate.delete(getUserRedisKey(getCurrentUserEmail()));
    }

    public static void cache(CurrentUserDTO user, StringRedisTemplate redisTemplate) {
        try {
            redisTemplate.opsForValue().set(
                    getUserRedisKey(user.getEmail()),
                    new ObjectMapper().writeValueAsString(user), 30, TimeUnit.MINUTES);
        } catch (JsonProcessingException e) {
            log.error("转JSON失败", e);
            throw new SystemException();
        }
    }

    public static void setCurrentUser(String email, StringRedisTemplate redisTemplate) {
        CurrentUserDTO user = getAuthInfo(email, redisTemplate);
        LOCAL_USER.set(user);
    }

    public static CurrentUserDTO getAuthInfo(String email, StringRedisTemplate redisTemplate) {
        String userJson = redisTemplate.opsForValue().get(getUserRedisKey(email));
        try {
            return new ObjectMapper().readValue(userJson, CurrentUserDTO.class);
        } catch (IOException e) {
            log.error("解析JSON失败", e);
            throw new AuthException();
        }
    }

    public static boolean authed(String token, StringRedisTemplate redisTemplate) {
        String email = JwtUtils.parseToEmail(token);
        Boolean authed = redisTemplate.hasKey(getUserRedisKey(email));
        if (authed != null && authed) {
            setCurrentUser(email, redisTemplate);
            flushExpireTime(redisTemplate);
            return true;
        }
        return false;
    }

    public static void flushExpireTime(StringRedisTemplate redisTemplate) {
        redisTemplate.expire(getUserRedisKey(getCurrentUserEmail()), 30, TimeUnit.MINUTES);
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

    private static String getUserRedisKey(String email) {
        return (USER_REDIS_KEY_PIX + email).toUpperCase();
    }

}
