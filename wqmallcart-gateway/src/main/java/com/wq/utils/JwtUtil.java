package com.wq.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Title: JwtUtil
 * @Description:
 * @Author: 魏秦
 * @Date: 2020/1/6  16:47
 */
public class JwtUtil {
    /**
     * 签名用的密钥
     */
    private static final String SIGNING_KEY = "78sebr72umyz33i9876gc31urjgyfhgj";

    public static final String jwtId = "tokenId";
    /**
     * 用户登录成功后生成Jwt
     * 使用HS256算法
     *
     * @param claims 保存在Payload（有效载荷）中的内容
     * @return token字符串
     */
    public static String createJWT(Map<String, Object> claims) {
        //指定签名算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //生成JWT的时间
        Date now = new Date(System.currentTimeMillis());
        SecretKey secretKey=generalKey();
        long nowMillis=System.currentTimeMillis();
        //创建一个JwtBuilder，设置jwt的body
        JwtBuilder builder = Jwts.builder()
                //保存在Payload中的内容
                .setClaims(claims)
                .setId(jwtId)
                //iat: jwt的签发时间
                .setIssuedAt(now)
                //设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, SIGNING_KEY);

        return builder.compact();
    }

    /***
     * 由字符串生成加密key
     * @return
     */
    private static SecretKey generalKey() {
        String stringKey=SIGNING_KEY;
        byte[] encodedKey= Base64.decodeBase64(stringKey);
        SecretKey key=new SecretKeySpec(encodedKey,0,encodedKey.length,"AES");
        return key;
    }

    /**
     * 解析token，获取到Payload中的内容，验证签名
     * @param token
     * @return
     */
    public static Claims verifyJwt(String token) {
        SecretKey key=generalKey();
        //得到DefaultJwtParser
        Claims claims = Jwts.parser()
                //设置签名的秘钥
                .setSigningKey(key)
                //设置需要解析的token
                .parseClaimsJws(token).getBody();
        return claims;
    }

    /***
     * 根据用户手机号生成token
     * @param userId
     * @return
     */
   public static String generateToken(String userId){
        Map<String,Object> map=new HashMap<>();
        map.put("userId",userId);
        return createJWT(map);
   }

}
