package com.generation153.harmonyfree.core.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.generation153.harmonyfree.core.security.filter.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}
	
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                		//PERMESSI CHIAMATE Playlist
                		.requestMatchers("/api/v1/playlists/**").hasRole("USER")
                		//PERMESSI CHIAMATE Stats
                		.requestMatchers(HttpMethod.GET, "/api/v1/stats/**").permitAll()
                		//PERMESSI CHIAMATE Track
                		.requestMatchers(HttpMethod.GET, "/api/v1/tracks/**").permitAll()
                        //PERMESSI CHIAMATE User
                        .requestMatchers("/api/v1/users/me", "/api/v1/users/me/**").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/users").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/users/**").hasRole("ADMIN")

                    .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
