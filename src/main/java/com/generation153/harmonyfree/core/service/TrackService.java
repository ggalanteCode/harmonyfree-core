package com.generation153.harmonyfree.core.service;

import java.util.List;

import com.generation153.harmonyfree.core.dto.TrackSearchResponse;
import com.generation153.harmonyfree.core.dto.TrackSearchRequest;

public interface TrackService {
	
	List<TrackSearchResponse> getTracks(TrackSearchRequest request);

	TrackSearchResponse getTrackById(Long jamendoTrackId);

}
