package com.generation153.harmonyfree.core.service;

import java.util.List;

import com.generation153.harmonyfree.core.dto.track.TrackPageResponse;
import com.generation153.harmonyfree.core.dto.track.TrackResponse;
import com.generation153.harmonyfree.core.dto.track.TrackSearchRequest;
import com.generation153.harmonyfree.core.dto.track.TrackSearchResponse;

public interface TrackService {
	
	TrackPageResponse getTracks(TrackSearchRequest request);
	
	List<TrackSearchResponse> getLatestTracks(int limit);

	TrackResponse getTrackById(Long jamendoTrackId);

}
