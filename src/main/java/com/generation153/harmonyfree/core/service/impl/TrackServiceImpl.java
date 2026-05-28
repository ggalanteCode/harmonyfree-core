package com.generation153.harmonyfree.core.service.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.generation153.harmonyfree.core.client.JamendoClient;
import com.generation153.harmonyfree.core.dto.TrackSearchResponse;
import com.generation153.harmonyfree.core.entity.Track;
import com.generation153.harmonyfree.core.exception.BadRequestException;
import com.generation153.harmonyfree.core.exception.ResourceNotFoundException;
import com.generation153.harmonyfree.core.dto.JamendoSearchResponse;
import com.generation153.harmonyfree.core.dto.JamendoTrackDto;
import com.generation153.harmonyfree.core.dto.JamendoTrackResponse;
import com.generation153.harmonyfree.core.dto.TrackResponse;
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
	            .map(dto -> mapToTrackSearchResponse(dto))
	            .toList();
		
	}
	
	private List<String> extractGenres(JamendoTrackDto dto) {

	    if (hasNoGenres(dto)) {
	        return List.of();
	    }

	    return dto.getMusicinfo().getTags().getGenres();
	}
	
	private boolean hasNoGenres(JamendoTrackDto dto) {
		return dto.getMusicinfo() == null 
				|| dto.getMusicinfo().getTags() == null 
				|| dto.getMusicinfo().getTags().getGenres() == null;
	}
	
	private TrackSearchResponse mapToTrackSearchResponse(JamendoTrackDto dto) {
		TrackSearchResponse response = new TrackSearchResponse(
                dto.getId(),              // jamendoId
                dto.getName(),
                dto.getArtistName(),
                dto.getAlbumName(),
                extractGenres(dto), //lista dei generi musicali
                dto.getDuration(),
                dto.getCoverUrl(),
                dto.getAudioUrl()
        );
		return response;
	}

	@Override
	public TrackResponse getTrackById(Long jamendoTrackId) {
		
		if (jamendoTrackId == null) {
	        throw new BadRequestException("Jamendo Track id is required");
	    }

	    // 1. DB lookup: ovvero cerca nel db se la traccia con l'id specificato esiste oppure no.
	    Optional<Track> existingTrack = trackRepository.findByJamendoTrackId(jamendoTrackId);

	    //se la traccia cercata esiste nel db
	    if (existingTrack.isPresent()) {
	    	log.info("getTrackById: track exists in db: " + existingTrack.get());
	    	log.info("getTrackById: genres: " + existingTrack.get().getGenres());
	    	//la mappiamo nella response e la restituiamo
	        return mapToTrackResponse(existingTrack.get());
	    }
	    
	    log.info("getTrackById: track does not exists in db: call jamendo API");

	    //altrimenti
	    // 2. Jamendo fallback: ovvero chiama Jamendo per cercare la traccia desiderata con quell'id
	    JamendoTrackResponse response = jamendoClient.getTrackById(jamendoTrackId);

	    if (response == null || response.getResults().isEmpty()) {
	        throw new ResourceNotFoundException("Track not found");
	    }
	    
	    log.info("getTrackById: track found in Jamendo API: " + response);

	    // 3. mapping DTO → entity
	    JamendoTrackDto dto = response.getResults().get(0);	//la traccia restituita è una sola
	    
	    log.info("getTrackById: genres: " + extractGenres(dto));
	    
	    Track track = mapToEntity(dto);

	    // 4. save (cache)
	    track = trackRepository.save(track);

	    return mapToTrackResponse(track);
		
	}
	
	private Track mapToEntity(JamendoTrackDto dto) {
		Track track = new Track();
	    track.setJamendoTrackId(dto.getId());
	    track.setJamendoArtistId(dto.getArtistId());
	    track.setJamendoAlbumId(dto.getAlbumId());
	    track.setTitle(dto.getName());
	    track.setArtistName(dto.getArtistName());
	    track.setAlbumName(dto.getAlbumName());
	    track.setGenres(extractGenres(dto));
	    track.setDuration(dto.getDuration());
	    track.setAudioUrl(dto.getAudioUrl());
	    track.setCoverUrl(dto.getCoverUrl());
	    track.setCreatedAt(LocalDateTime.now());
	    return track;
	}

	private TrackResponse mapToTrackResponse(Track track) {
		TrackResponse response = new TrackResponse(
				track.getId(), 
				track.getJamendoTrackId(), 
				track.getTitle(), 
				track.getArtistName(), 
				track.getAlbumName(), 
				track.getGenres() != null ? track.getGenres() : List.of(), 
				track.getDuration(), 
				track.getCoverUrl(), 
				track.getAudioUrl(), 
				track.getCreatedAt()
		);
	    return response;
	}

}
