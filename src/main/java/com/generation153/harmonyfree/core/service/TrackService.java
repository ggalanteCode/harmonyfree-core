package com.generation153.harmonyfree.core.service;

import java.util.List;

import com.generation153.harmonyfree.core.dto.TrackResponse;

public interface TrackService {
	
	List<TrackResponse> getTracks(
	        String query,
	        String title,
	        String artist,
	        String album,
	        String genre,
	        int offset,
	        int limit
	    );

	TrackResponse getTrackById(Long jamendoTrackId);

}
