package com.akehcloud.iecube.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

/**
 * JWT工具类
 *
 * @author panghaoyue
 */
public class JwtUtils {

    private static final String SECRET = "aksjdflajs";
    private static final String USERNAME_STR = "username";
    private static final String EMAIL_STR = "email";

    private JwtUtils() {
    }

    public static String toJwtToken(String email) {
        return JWT
                .create()
                .withClaim(EMAIL_STR, email)

                .sign(Algorithm.HMAC256(SECRET));
    }

    public static String parseToEmail(String token) {
        return JWT.decode(token).getClaim(EMAIL_STR).asString();
    }

    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim(USERNAME_STR, username)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 生成签名,5min后过期
     *
     * @param username 用户名
     * @param secret   用户的密码
     * @return 加密的token
     */
    public static String sign(String username, String secret) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 附带username信息
        return JWT.create()
                .withClaim("username", username)
                .sign(algorithm);
    }

    public static String sign(Long value, String secret) {
        // 附带username信息
        return JWT.create()
                .withClaim("value", value)
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC256(secret));
    }

    public static <T> T parse(String token, String secret, Class<T> clazz) {
        return JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token)
                .getClaim("value").as(clazz);
    }

}
