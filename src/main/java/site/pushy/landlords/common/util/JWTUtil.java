package site.pushy.landlords.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import site.pushy.landlords.common.config.properties.LandlordsProperties;

import java.util.Calendar;
import java.util.Date;

@Service
public class JWTUtil implements ApplicationContextAware {

    private static String SECRET;

    /**
     * 编码,通过用户的userId编码为Token
     */
    public static String encode(String id) {
        // expire time
        Calendar nowTime = Calendar.getInstance();
        //有10天有效期
        nowTime.add(Calendar.DATE, 10);
        Date expiresDate = nowTime.getTime();

        Claims claims = Jwts.claims();
        claims.put("userId", id);
        claims.setAudience("cy");
        claims.setIssuer("cy");
        return Jwts.builder().setClaims(claims).setExpiration(expiresDate)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    /**
     * 解码, 通过Token解码出用户的userId
     */
    public static String decode(String token) {
        Jws<Claims> jws = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
        Claims claims = jws.getBody();
        return (String) claims.get("userId");
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        LandlordsProperties properties = ctx.getBean(LandlordsProperties.class);
        if (StringUtils.isEmpty(properties.getJwtSecret())) {
            throw new IllegalArgumentException("The secret cannot be empty");
        }
        SECRET = properties.getJwtSecret();
    }
}
