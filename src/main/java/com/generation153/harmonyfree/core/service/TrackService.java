package com.generation153.harmonyfree.core.service;

import java.util.List;

import com.generation153.harmonyfree.core.dto.track.TrackResponse;
import com.generation153.harmonyfree.core.dto.track.TrackSearchRequest;
import com.generation153.harmonyfree.core.dto.track.TrackSearchResponse;

public interface TrackService {
	
	List<TrackSearchResponse> getTracks(TrackSearchRequest request);

	TrackResponse getTrackById(Long jamendoTrackId);

}
