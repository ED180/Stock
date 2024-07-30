package com.example.edproject.util;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;


import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.stereotype.Component;


import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Component
public class JwtUtil {
//    private static final String secretKey = "your_secret_key";
//
//    public static String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    public Integer extractUserId(String token) {
//        Claims claims = extractAllClaims(token);
//        return claims.get("userId", Integer.class);
//    }
//
//    public static  <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//    private static Claims extractAllClaims(String token) {
//        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
//    }
//
//    public static Boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    public static Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    public static String generateToken(String email, Long userId) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("userId", userId);
//        return createToken(claims, email);
//    }
//
//    private static String createToken(Map<String, Object> claims, String subject) {
//        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
//                .signWith(SignatureAlgorithm.HS256, secretKey).compact();
//    }
//
//    public static Boolean validateToken(String token, String email) {
//        final String extractedUsername = extractUsername(token);
//        return (extractedUsername.equals(email) && !isTokenExpired(token));
//    }

    private static final String KEY = "stock";
    public static String genToken(Map<String, Object> claims) {
        return JWT.create()
                .withClaim("claims", claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 *12))
                .sign(Algorithm.HMAC256(KEY));
    }

    public static Map<String,Object> parseToken(String token) {
        return JWT.require(Algorithm.HMAC256(KEY))
                .build()
                .verify(token)
                .getClaim("claims")
                .asMap();
    }

}
