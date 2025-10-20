package com.mu54omd.mini_ecommerce.backend_maven.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode("a3NkZmpzaWZoaWVmaDM4MzdmZGhudmpza2RoZjgzZmhzZmpmc2pmI0Aj"));
    private final long EXPIRATION_MS = 1L * 60L * 60L * 1000L;  // 1 hr * 60 minutes * 60 seconds * 1000 milliseconds

    public String generateToken(String username){
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        try {
            return getClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    private Claims getClaims(String token) {
        String rawToken = token;
        if(token.startsWith("Bearer ")) {
            rawToken = token.replaceFirst("Bearer ", "");
        }
        Claims claims = null;
        try{
            claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(rawToken)
                    .getPayload();
        }catch (Exception ignored){

        }
        return claims;
    }
}
