package com.generation153.harmonyfree.core.service;

import java.util.List;

import com.generation153.harmonyfree.core.dto.stats.TrackStatsResponse;

public interface StatsService {

    List<TrackStatsResponse> getMostPopularTracks(int limit);
    
    List<TrackStatsResponse> getMostFavoritedTracks(int limit);

}