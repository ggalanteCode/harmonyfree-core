package com.generation153.harmonyfree.core.service.impl;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.generation153.harmonyfree.core.dto.stats.TrackStatsResponse;
import com.generation153.harmonyfree.core.entity.Track;
import com.generation153.harmonyfree.core.exception.BadRequestException;
import com.generation153.harmonyfree.core.repository.TrackRepository;
import com.generation153.harmonyfree.core.service.StatsService;

@Service
public class StatsServiceImpl implements StatsService {

    private final TrackRepository trackRepository;

    public StatsServiceImpl(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    // TRACK PIU UTILIZZATE

    @Override
    public List<TrackStatsResponse> getMostPlayedTracks(int limit) {

        if (limit <= 0) {
            throw new BadRequestException("Limit must be > 0");
        }

        List<Object[]> results =
                trackRepository.findMostPlayedTracks(
                        PageRequest.of(0, limit));

        return results.stream()
                .map(obj -> {

                    Track track = (Track) obj[0];

                    Long score =
                            ((Number) obj[1]).longValue();

                    return new TrackStatsResponse(
                            track.getId(),
                            track.getTitle(),
                            track.getArtistName(),
                            track.getAlbumName(),
                            score
                    );
                })
                .toList();
    }
    
 // TRACK PIU AGGIUNTE AI PREFERITI

    @Override
    public List<TrackStatsResponse> getMostFavoritedTracks(int limit) {

        if (limit <= 0) {
            throw new BadRequestException("Limit must be > 0");
        }

        List<Object[]> results =
                trackRepository.findMostFavoritedTracks(
                        PageRequest.of(0, limit));

        return results.stream()
                .map(obj -> {

                    Track track = (Track) obj[0];

                    Long count =
                            ((Number) obj[1]).longValue();

                    return new TrackStatsResponse(
                            track.getId(),
                            track.getTitle(),
                            track.getArtistName(),
                            track.getAlbumName(),
                            count
                    );
                })
                .toList();
    }
}