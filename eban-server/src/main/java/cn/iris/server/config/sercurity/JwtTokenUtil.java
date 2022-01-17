package cn.iris.server.config.sercurity;

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

    private static final String CLATH_KEY_USERNAME = "sub";
    private static final String CLATH_KEY_CREATED = "created";

    /**
     * 通过配置文件获取密钥内容
     */
    @Value("${jwt.secret}")
    private String secret;
    /**
     * 获取令牌失效时间
     */
    @Value("${jwt.expiration}")
    private Long expiration;


    /**
     * 根据用户信息生成Token
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLATH_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLATH_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 从Token中获取用户名
     * @param token
     * @return
     */
    public String getUserNameByToken(String token) {
        String userName;
        try {
            Claims claims = getClaimsByToken(token);
            userName = claims.getSubject();
        } catch (Exception e) {
            userName = null;
        }
        return userName;
    }

    /**
     * 判断Token是否有效（是否过期，用户名是否相同）
     * @param token
     * @param userDetails
     * @return
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String userName = getUserNameByToken(token);
        return userName.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * 判断Token能否被刷新(Token失效)
     * @param token
     * @return
     */
    public boolean canRefreshToken(String token) {
        return !isTokenExpired(token);
    }

    /**
     * 刷新Token
     * @param token
     * @return
     */
    public String refreshToken(String token) {
        Claims claims = getClaimsByToken(token);
        claims.put(CLATH_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 判断Token是否失效
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateByToken(token);
        return expiredDate.before(new Date());
    }

    /**
     * 从Token中获取失效时间
     * @param token
     * @return
     */
    private Date getExpiredDateByToken(String token) {
        Claims claims = getClaimsByToken(token);
        return claims.getExpiration();
    }

    /**
     * 从Token中获取JwtToken
     * @param token
     * @return
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
     * 根据荷载生成JwtToken
     * @param claims
     * @return
     */
    private String generateToken(Map<String, Object> claims) {

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.ES512, secret)
                .compact();
    }

    /**
     * 生成Token失效时间
     * @return
     */
    private Date generateExpirationDate() {
        // 系统当前时间 + 设置的失效时限
        return new Date(System.currentTimeMillis()+expiration*1000);
    }

}
