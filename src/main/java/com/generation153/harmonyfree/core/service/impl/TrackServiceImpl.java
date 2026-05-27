package com.generation153.harmonyfree.core.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.generation153.harmonyfree.core.client.JamendoClient;
import com.generation153.harmonyfree.core.dto.TrackSearchResponse;
import com.generation153.harmonyfree.core.dto.JamendoSearchResponse;
import com.generation153.harmonyfree.core.dto.TrackSearchRequest;
import com.generation153.harmonyfree.core.repository.TrackRepository;
import com.generation153.harmonyfree.core.service.TrackService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TrackServiceImpl implements TrackService {
	
	private final TrackRepository trackRepository;
	
	private final JamendoClient jamendoClient;

	public TrackServiceImpl(TrackRepository trackRepository, JamendoClient jamendoClient) {
		this.trackRepository = trackRepository;
		this.jamendoClient = jamendoClient;
	}

	@Override
	public List<TrackSearchResponse> getTracks(TrackSearchRequest request) {
		
		//chiamata al client
	    JamendoSearchResponse response = jamendoClient.searchTracks(request);
	    
	    log.info("response: "+response);

	    //gestione empty/null
	    if (response == null || response.getResults() == null || response.getResults().isEmpty()) {
	        return Collections.emptyList();
	    }

	    //mapping
	    return response.getResults().stream()
	            .map(dto -> new TrackSearchResponse(
	                    dto.getId(),              // jamendoId
	                    dto.getName(),
	                    dto.getArtistName(),
	                    dto.getAlbumName(),
	                    dto.getTags().getGenres().get(0) //
	            ))
	            .toList();
		
	}
	
	@Override
	public TrackSearchResponse getTrackById(Long jamendoTrackId) {
		// TODO Auto-generated method stub
		return null;
	}

}
