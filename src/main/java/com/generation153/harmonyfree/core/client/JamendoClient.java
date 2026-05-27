package com.generation153.harmonyfree.core.client;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.generation153.harmonyfree.core.config.JamendoProperties;
import com.generation153.harmonyfree.core.dto.JamendoSearchResponse;
import com.generation153.harmonyfree.core.dto.TrackSearchRequest;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JamendoClient {

    private final WebClient webClient;
    private final String clientId;

    public JamendoClient(WebClient webClient, JamendoProperties props) {
        this.webClient = webClient;
        this.clientId = props.getClientId();
    }
    
    public JamendoSearchResponse searchTracks(TrackSearchRequest request) {
    	
    	log.info("Jamendo clientId: " + clientId);
    	
    	return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/tracks")
                        .queryParam("client_id", clientId)
                        .queryParam("format", "json")
                        .queryParam("limit", Math.min(request.getLimit(), 200))
                        .queryParam("offset", request.getOffset())
                        .queryParamIfPresent("search", Optional.ofNullable(request.getQuery()))
                        .queryParamIfPresent("name", Optional.ofNullable(request.getTitle()))
                        .queryParamIfPresent("artist_name", Optional.ofNullable(request.getArtist()))
                        .queryParamIfPresent("album_name", Optional.ofNullable(request.getAlbum()))
                        .queryParamIfPresent("tags", Optional.ofNullable(request.getGenre()))
                        .queryParam("include", "musicinfo")
                        .build())
                .retrieve()
                .bodyToMono(JamendoSearchResponse.class)
                .block();
    	
    }
    
}
