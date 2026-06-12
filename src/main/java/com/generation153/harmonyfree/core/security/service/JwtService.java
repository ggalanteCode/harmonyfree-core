package com.generation153.harmonyfree.core.security.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.List;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Service
public class JwtService {
	
	@Value("${jwt.secret}")
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

    public Integer extractAuthUserId(String token) {
    	
        Object idClaim = extractAllClaims(token).get("id");
        
        if (idClaim instanceof Integer value) {
            return value;
        }
        if (idClaim instanceof Long value) {
            return value.intValue();
        }
        if (idClaim instanceof String value) {
            return Integer.parseInt(value);
        }

        throw new RuntimeException("Claim id non valido");
        
    }

	public boolean isTokenValid(String token) {
		try {
            extractAllClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
	}

	public List<String> extractRoles(String token) {
		List<?> roles = extractAllClaims(token).get("roles", List.class);
        return roles.stream()
                .map(Object::toString)
                .toList();
	}
	
	public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

}
