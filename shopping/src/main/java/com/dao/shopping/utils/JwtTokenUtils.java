package com.dao.shopping.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

@Service
public class JwtTokenUtils {

    @Value("${token.secret-key}")
    private String SECRET_KEY;

    @Value("${token.access.expiration}")
    private long EXPIRATION_TIME;

    @Value("${token.refresh.expiration}")
    private Long REFRESH_TOKEN_EXPIRATION;

    public String generateToken(UserDetails userDetails, Long expirationTime){
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .claim("authorities", userDetails.getAuthorities())
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

    }

    public String getUserEmailFromToken(String token) {
        return getClaimsFromToken(token, Claims::getSubject);
    }

    private <T> T getClaimsFromToken(String token, Function<Claims, T> claimResolver){
        final Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        return claimResolver.apply(claims);
    }

    public boolean validate(String token, UserDetails userDetails) {
        String userEmail = getUserEmailFromToken(token);
        return (userEmail.equalsIgnoreCase(userDetails.getUsername()) && !isExpired(token));
    }

    public String generateAccessToken(UserDetails userDetails) {
            return generateToken(userDetails, EXPIRATION_TIME);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails, REFRESH_TOKEN_EXPIRATION);
    }

    private boolean isExpired(String token) {
        Date expired = getClaimsFromToken(token, Claims::getExpiration);
        return expired.before(new Date());
    }
}
