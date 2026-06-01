package com.generation153.harmonyfree.core.security.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.generation153.harmonyfree.core.security.model.CustomUserPrincipal;
import com.generation153.harmonyfree.core.security.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		//RECUPERA L'HEADER DELLA REQUEST
		String header = request.getHeader("Authorization");

		//SE IL TOKEN NON E' NULLO ED INIZIA CON Bearer
		if (header != null && header.startsWith("Bearer ")) {
			
			//TOGLI Bearer DAL TOKEN
	        String token = header.substring(7);

	        // 👉 1. valida token
	        if (jwtService.isTokenValid(token)) {

	            // 👉 2. estrai dati dal token
	            Integer userId = jwtService.extractAuthUserId(token);
	            String email = jwtService.extractEmail(token);
	            List<String> roles = jwtService.extractRoles(token);

	            // 👉 3. traduci le stringhe dei ruoli estratte dal token 
	            //		 in SimpleGrantedAuthority, comprensibili da Spring Security
	            List<SimpleGrantedAuthority> authorities = roles.stream()
	                .map(SimpleGrantedAuthority::new)
	                .toList();
	            
	            CustomUserPrincipal principal = new CustomUserPrincipal(userId, email);

		        // 👉 crea un oggetto Authentication per Spring Security,
		        //usando l'userId come principal e le authorities estratte dal JWT.
		        //la password è null perché l'autenticazione è già avvenuta tramite token
	            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
	            	principal,
	                null,
	                authorities
	            );

	            SecurityContextHolder.getContext().setAuthentication(auth);
	        }
	    }

	    filterChain.doFilter(request, response);
		
	}

}
