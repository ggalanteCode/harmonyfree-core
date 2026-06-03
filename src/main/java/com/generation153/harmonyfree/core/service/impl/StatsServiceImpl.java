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
    public List<TrackStatsResponse> getMostPopularTracks(int limit) {

    	// Verifica che il numero massimo di risultati richiesti sia valido
        if (limit <= 0) {
            throw new BadRequestException("Limit must be > 0");
        }

        // Esegue la query recuperando le track ordinate per utilizzo
        // limit determina il numero massimo di record restituiti
        List<Object[]> results =
                trackRepository.findMostPopularTracks(
                        PageRequest.of(0, limit));

        // Trasforma il risultato grezzo della query in DTO
        return results.stream()
                .map(obj -> {

                	// Primo elemento dell'array: Track
                    Track track = (Track) obj[0];

                    // Secondo elemento dell'array: score calcolato dalla query
                    Long score =
                            ((Number) obj[1]).longValue();

                    // Costruzione del DTO restituito al controller
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

    	// Verifica che il numero massimo di risultati richiesti sia valido
        if (limit <= 0) {
            throw new BadRequestException("Limit must be > 0");
        }

        // Esegue la query recuperando le track ordinate per numero di preferiti
        List<Object[]> results =
                trackRepository.findMostFavoritedTracks(
                        PageRequest.of(0, limit));

        // Trasforma il risultato grezzo della query in DTO
        return results.stream()
                .map(obj -> {

                	// Primo elemento dell'array: Track
                    Track track = (Track) obj[0];

                    // Secondo elemento dell'array: numero di preferiti
                    Long count =
                            ((Number) obj[1]).longValue();

                    // Costruzione del DTO restituito al controller
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