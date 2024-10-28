package com.kojikoji.gpt.domain.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.spi.IIOServiceProvider;
import java.util.*;

/**
 * @ClassName JwtUtil
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/10/27 21:46
 * @Version
 */
@Slf4j
public class JwtUtil {

    // 创建默认的密钥和算法，供无参的构造方法使用
    public static final String defaultBase64EncodedSecretKey = "B*B^";

    public static final SignatureAlgorithm defaultSignatureAlgorithm = SignatureAlgorithm.HS256;

    private final String base64EncodedSecretKey;
    private final SignatureAlgorithm signatureAlgorithm;

    public JwtUtil() {
        this(defaultBase64EncodedSecretKey, defaultSignatureAlgorithm);
    }

    public JwtUtil(String base64EncodedSecretKey, SignatureAlgorithm signatureAlgorithm) {
        this.base64EncodedSecretKey = Base64.encodeBase64String(base64EncodedSecretKey.getBytes());
        this.signatureAlgorithm = signatureAlgorithm;
    }

    /**
     * 生成jwt字符串
     * 包括：
     *  1. header
     *      - 字符串的类型，一般都是"JWT"
     *      - 使用的加密算法，"HS256"
     *      一般都是固定的
     *  2. payload
     *      一般有四个标准字段
     *      iat: 签发时间
     *      jti: 唯一标识
     *      iss: 签发人
     *      exp: 过期时间
     */
    public String encode(String issuer, long ttlMillis, Map<String, Object> claim) {
        // iss签发人，ttl生存时间，claim-在jwt中存储的一些非隐私信息
        if (Objects.isNull(claim)) {
            claim = new HashMap<>();
        }
        long nowMillis = System.currentTimeMillis();

        log.info("sKey {}", base64EncodedSecretKey);
        JwtBuilder builder = Jwts.builder()
                // 荷载
                .setClaims(claim)
                // JWT 唯一标识
                .setId(UUID.randomUUID().toString())
                // 签发时间
                .setIssuedAt(new Date(nowMillis))
                // 签发人，一般是username或者userId
                .setSubject(issuer)
                .signWith(signatureAlgorithm, base64EncodedSecretKey);
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis); // 过期时间
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    // 传入token解出对应的用户信息
    public Claims decode(String jwtToken) {
        return Jwts.parser()
                .setSigningKey(base64EncodedSecretKey)
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    // 判断jwtToken是否合法
    public boolean isVerify(String jwtToken) {
        Algorithm algorithm = null;
        switch (signatureAlgorithm) {
            case HS256:
                algorithm = Algorithm.HMAC256(Base64.decodeBase64(base64EncodedSecretKey));
                break;
            default:
                throw new RuntimeException("不支持该算法");
        }
        JWTVerifier verifier = JWT.require(algorithm).build();
        verifier.verify(jwtToken);
        // 不合法会抛出异常
        // 合法标准：1. 头部和荷载没有被篡改 2.没有过期
        return true;
    }
}
