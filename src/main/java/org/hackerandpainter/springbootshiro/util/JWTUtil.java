package org.hackerandpainter.springbootshiro.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.Date;


/**
 * @Description
 * @Author Gao Hang Hang
 * @Date 2019-05-25 14:35
 **/
public class JWTUtil {

    // 过期时间5分钟
    private static final long EXPIRE_TIME = 5*60*1000;

    /**
     * 生成签名,5min后过期
     * @return
     */
    public static String sign(String username, String secret) {
        try {
            Date data = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(secret); // 使用HMAC256算法加密
            // 附带username信息
            return JWT.create()
                    .withClaim("username", username) // withClaim用来放置需要传输的信息
                    .withExpiresAt(data)  // 设置过期时间
                    .sign(algorithm); // 签名
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 校验token是否正确
     * @param token
     * @param username
     * @param secret
     * @return
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret); // 算法
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    .build();

            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     * @param token
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }
}
