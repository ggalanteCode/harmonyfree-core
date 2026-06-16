package com.generation153.harmonyfree.core.service;

import com.generation153.harmonyfree.core.dto.track.TrackPageResponse;
import com.generation153.harmonyfree.core.dto.track.TrackResponse;
import com.generation153.harmonyfree.core.dto.track.TrackSearchRequest;

public interface TrackService {
	
	TrackPageResponse getTracks(TrackSearchRequest request);

	TrackResponse getTrackById(Long jamendoTrackId);

}
