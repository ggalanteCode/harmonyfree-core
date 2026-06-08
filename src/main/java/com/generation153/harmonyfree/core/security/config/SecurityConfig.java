package com.generation153.harmonyfree.core.security.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
        		.cors(cors -> cors.configurationSource(corsConfigurationSource()))
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
                        //PERMETTERE AL FRONTEND L'ACCESSO ALLE IMMAGINI PROFILO SALVATE NEL BACKEND
                        .requestMatchers("/uploads/**").permitAll()
                    .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
	
	@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        
		CorsConfiguration configuration = new CorsConfiguration();
        
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:5500",
            "http://192.168.1.107:5500"
        ));
        
        configuration.setAllowedMethods(Arrays.asList(
        		"GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
