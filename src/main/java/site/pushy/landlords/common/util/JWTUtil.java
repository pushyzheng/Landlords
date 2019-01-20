package site.pushy.landlords.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Calendar;
import java.util.Date;


public class JWTUtil {

    private static final String SECRET = "DyoonSecret_0581";

    /**
     * 编码,通过用户的userId编码为Token
     * @param id
     */
    public static String encode(String id) {
        Date iatDate = new Date();
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
     * @param token
     */
    public static String decode(String token) {
        Jws<Claims> jws = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
        Claims claims = jws.getBody();
        return (String) claims.get("userId");
    }


}
