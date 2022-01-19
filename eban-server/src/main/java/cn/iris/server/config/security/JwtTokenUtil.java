package cn.iris.server.config.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JwtToken工具类
 * @author Iris 2022/1/17
 */
@Component
public class JwtTokenUtil {

    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 根据用户信息生成Token
     * @param userDetails 用户信息
     * @return token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 判断Token是否有效（是否过期 && 用户名是否正确）
     * @param token token
     * @param userDetails 用户信息
     * @return True | False
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUserNameByToken(token);
        return username.equals(userDetails.getUsername()) && isTokenUnexpired(token);
    }

    /**
     * 判断Token能否被刷新(Token失效)
     * @param token token
     * @return True | False
     */
    public boolean canRefreshToken(String token) {
        return isTokenUnexpired(token);
    }

    /**
     * 刷新token
     * @param token token
     * @return 新的token
     */
    public String refreshToken(String token) {
        Claims claims = getClaimsByToken(token);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 从token中获取用户名
     * @param token token
     * @return 用户名
     */
    public String getUserNameByToken(String token) {
        String username;
        try {
            Claims claims = getClaimsByToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 判断Token是否失效
     * @param token token
     * @return True:未失效 | False:失效
     */
    private boolean isTokenUnexpired(String token) {
        Date expiredDate = getExpiredDateByToken(token);
        return !expiredDate.before(new Date());
    }


    /**
     * 从token中获取失效时间
     * @param token token
     * @return 从token中获取的失效时间
     */
    private Date getExpiredDateByToken(String token) {
        Claims claims = getClaimsByToken(token);
        return claims.getExpiration();
    }

    /**
     * 从Token中获取JwtToken
     * @param token token
     * @return JwtToken
     */
    private Claims getClaimsByToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claims;
    }

    /**
     * 根据载荷生成JwtToken
     * @param claims 载荷
     * @return JwtToken
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 生成Token失效时间
     * @return 时间
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis()+expiration*1000);
    }
}
