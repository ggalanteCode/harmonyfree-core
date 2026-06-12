package com.generation153.harmonyfree.core.service.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.generation153.harmonyfree.core.client.JamendoClient;
import com.generation153.harmonyfree.core.dto.jamendo.JamendoSearchResponse;
import com.generation153.harmonyfree.core.dto.jamendo.JamendoTrackDto;
import com.generation153.harmonyfree.core.dto.jamendo.JamendoTrackResponse;
import com.generation153.harmonyfree.core.dto.track.TrackResponse;
import com.generation153.harmonyfree.core.dto.track.TrackSearchRequest;
import com.generation153.harmonyfree.core.dto.track.TrackSearchResponse;
import com.generation153.harmonyfree.core.entity.Genre;
import com.generation153.harmonyfree.core.entity.Track;
import com.generation153.harmonyfree.core.exception.BadRequestException;
import com.generation153.harmonyfree.core.exception.ResourceNotFoundException;
import com.generation153.harmonyfree.core.repository.GenreRepository;
import com.generation153.harmonyfree.core.repository.TrackRepository;
import com.generation153.harmonyfree.core.service.TrackService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TrackServiceImpl implements TrackService {
	
	private final TrackRepository trackRepository;
	
	private final GenreRepository genreRepository;
	
	private final JamendoClient jamendoClient;

	public TrackServiceImpl(TrackRepository trackRepository, 
			GenreRepository genreRepository, 
			JamendoClient jamendoClient) {
		this.trackRepository = trackRepository;
		this.genreRepository = genreRepository;
		this.jamendoClient = jamendoClient;
	}

	@Override
	public List<TrackSearchResponse> getTracks(TrackSearchRequest request) {
		
		//chiamata al client
	    JamendoSearchResponse response = jamendoClient.searchTracks(request);
	    
	    log.info("response: "+response);

	    //gestione empty/null
	    if (response == null || response.getResults() == null || response.getResults().isEmpty()) {
	    	log.info("response list empty");
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
                dto.getAudioUrl(),
                dto.getDownloadUrl(),
                dto.getDownloadable()
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
	    track.setDuration(dto.getDuration());
	    track.setAudioUrl(dto.getAudioUrl());
	    track.setCoverUrl(dto.getCoverUrl());
	    track.setCreatedAt(LocalDateTime.now());
	    
	    track.setDownloadUrl(dto.getDownloadUrl());
	    track.setDownloadable(dto.getDownloadable());

	    List<String> genreNames = extractGenres(dto);

	    genreNames.forEach(name -> {
	    	
	    	//per non avere generi con nome uguale che distinguono tra maiuscole e minuscole
	    	String normalizedName = name.trim().toLowerCase();
	    	
	        Genre genre = genreRepository.findByName(normalizedName)
	            .orElseGet(() -> genreRepository.save(new Genre(normalizedName)));
	        
	        track.addGenre(genre);
	        
	    });

	    return track;
		
	}

	private TrackResponse mapToTrackResponse(Track track) {
		
		TrackResponse response = new TrackResponse(
				track.getId(), 
				track.getJamendoTrackId(), 
				track.getTitle(), 
				track.getArtistName(), 
				track.getAlbumName(), 
				
				track.getTrackGenres()
				.stream()
			    .map(tg -> tg.getGenre().getName())
			    .toList(), 
			    
				track.getDuration(), 
				track.getCoverUrl(), 
				track.getAudioUrl(), 
				track.getCreatedAt(),
				track.getDownloadUrl(),
				track.getDownloadable()
		);
		
	    return response;
	    
	}

}
