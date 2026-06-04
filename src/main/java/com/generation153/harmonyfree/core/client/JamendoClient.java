package com.generation153.harmonyfree.core.client;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.generation153.harmonyfree.core.config.JamendoProperties;
import com.generation153.harmonyfree.core.dto.jamendo.JamendoSearchResponse;
import com.generation153.harmonyfree.core.dto.jamendo.JamendoTrackResponse;
import com.generation153.harmonyfree.core.dto.track.TrackSearchRequest;

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
    	
    	boolean hasFilters = searchHasFilters(request);

        return webClient.get()
                .uri(uriBuilder -> {

                    var builder = uriBuilder
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
                            .queryParam("include", "musicinfo");

                    //se la search non ha dei filtri di ricerca, 
                    //ordina i risultati per data di pubblicazione decrescente, 
                    //altrimenti mantieni l'ordinamento di default, ovvero per "rilevanza" di Jamendo
                    if (!hasFilters) {
                        builder.queryParam("order", "releasedate_desc");
                    }

                    return builder.build();
                })
                .retrieve()
                .bodyToMono(JamendoSearchResponse.class)
                .block();
    	
    }

	private boolean searchHasFilters(TrackSearchRequest request) {
		return request.getQuery() != null ||
                request.getTitle() != null ||
                request.getArtist() != null ||
                request.getAlbum() != null ||
                request.getGenre() != null;
	}

	public JamendoTrackResponse getTrackById(Long jamendoTrackId) {
		
		log.info("Jamendo clientId: " + clientId);
		
		return webClient.get()
	            .uri(uriBuilder -> uriBuilder
	                    .path("/tracks")
	                    .queryParam("client_id", clientId)
	                    .queryParam("id", jamendoTrackId)
	                    .queryParam("format", "json")
	                    .queryParam("include", "musicinfo")
	                    .build())
	            .retrieve()
	            .bodyToMono(JamendoTrackResponse.class)
	            .block();
	}
    
}
