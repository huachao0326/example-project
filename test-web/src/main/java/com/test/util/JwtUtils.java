package com.test.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hb.trust.entity.JwtKey;
import com.hb.trust.mapper.JwtKeyMapper;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Base64;

/**
 * 功能描述: JWT
 *
 * @author huachao
 * @create 2022/3/30 16:48
 * @since 1.0
 */
@Slf4j
@Component
public class JwtUtils {

    @Resource
    JwtKeyMapper jwtKeyMapper;

    public static final int JWT_AVAILABLE_MINUTE = 10;

    static {
        if (Security.getProvider("BC") == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    public static JwtUtils jwtUtils;

    @PostConstruct
    public void init() {
        jwtUtils = this;
    }

    /**
     * 验证签名
     *
     * @param token: 必须包含iss(公钥base64),iat(签名时间, 到秒的long), data
     * @param <T>
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws InvalidKeySpecException
     */
    public <T> JwtDecoded verify(String token) throws JwtException {
        if (token == null) {
            String errorMsg = "verify failed: token is null";
            throw new JwtException(errorMsg);
        }
        String[] splits = token.split("\\.");
        if (splits.length != 3) {
            String errorMsg = "verify failed: token has invalid format";
            throw new JwtException(errorMsg);
        }
        var payload = splits[1];
        String jsonPayload = new String(Base64.getDecoder().decode(payload), StandardCharsets.UTF_8);
        String publicKey = "";
        String dataJSON = "";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            var payloadTree = objectMapper.readTree(jsonPayload);
            publicKey = payloadTree.get("iss").asText();
            if (Strings.isEmpty(publicKey)) {
                throw new JwtException("decode public key(claim iss) failed");
            }
            long iat = payloadTree.get("iat").asLong();
            LocalDateTime signTime = LocalDateTime.ofEpochSecond(iat, 0, ZoneOffset.of("+8"));
            if (signTime.plusMinutes(JWT_AVAILABLE_MINUTE).isBefore(LocalDateTime.now(ZoneId.systemDefault()))) {
                throw new JwtException("jwt sign expired, should be used in " + JWT_AVAILABLE_MINUTE + " minutes");
            }
            dataJSON = payloadTree.get("data").toString();
        } catch (JsonProcessingException e) {
            throw new JwtException("decode payload json failed");
        }

        PublicKey jwtPublicKey = null;
        try {
            jwtPublicKey = KeyFactory.
                    getInstance("EC", "BC").
                    generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey)));
        } catch (InvalidKeySpecException e) {
            throw new JwtException(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new JwtException(e.getMessage());
        } catch (NoSuchProviderException e) {
            throw new JwtException(e.getMessage());
        }

        try {
            JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(jwtPublicKey).build();
            Jws<Claims> parsed = jwtParser.parseClaimsJws(token);
            String alg = parsed.getHeader().get("alg").toString();
            if (Strings.isEmpty(alg)) {
                throw new JwtException("alg not set");
            }
            Object typ = parsed.getHeader().get("typ");
            LambdaQueryWrapper<JwtKey> jwtKeyLambdaQueryWrapper = new LambdaQueryWrapper<>();
            jwtKeyLambdaQueryWrapper.eq(JwtKey::getPublicKey, publicKey);
            jwtKeyLambdaQueryWrapper.eq(JwtKey::isStatus, Boolean.TRUE);
            jwtKeyLambdaQueryWrapper.eq(JwtKey::getAlg, alg);
            if(null != typ){
                jwtKeyLambdaQueryWrapper.eq(JwtKey::getTyp, typ);
            }
            JwtKey jwtKey = jwtUtils.jwtKeyMapper.selectOne(jwtKeyLambdaQueryWrapper);
            if (null == jwtKey) {
                throw new JwtException("verify failed: token is not allow");
            }
            return JwtDecoded.builder()
                    .alg(parsed.getHeader().get("alg").toString())
                    .pubKey(parsed.getBody().get("iss").toString())
                    .data(dataJSON)
                    .build();
        } catch (Exception e) {
            throw new JwtException("verify failed: ", e);
        }
    }

}
