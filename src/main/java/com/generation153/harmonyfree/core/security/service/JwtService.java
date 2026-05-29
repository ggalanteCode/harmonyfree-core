package com.generation153.harmonyfree.core.security.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class JwtService {
	
	@Value("")
	private String secret; // stessa dell'auth

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
    
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long extractAuthUserId(String token) {
    	
        Object idClaim = extractAllClaims(token).get("id");
        
        if (idClaim instanceof Integer value) {
            return value.longValue();
        }
        if (idClaim instanceof Long value) {
            return value;
        }
        if (idClaim instanceof String value) {
            return Long.parseLong(value);
        }

        throw new RuntimeException("Claim id non valido");
        
    }

}
