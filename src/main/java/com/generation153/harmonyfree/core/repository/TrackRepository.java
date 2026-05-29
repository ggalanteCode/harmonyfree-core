package com.generation153.harmonyfree.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation153.harmonyfree.core.entity.Track;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface TrackRepository extends JpaRepository<Track, Long> {

	Optional<Track> findByJamendoTrackId(Long jamendoTrackId);

	@Query("""
			    SELECT t,
			           COUNT(DISTINCT pt.id) + COUNT(DISTINCT uft.id)
			    FROM Track t
			    LEFT JOIN PlaylistTrack pt ON pt.track = t
			    LEFT JOIN UserFavoriteTrack uft ON uft.track = t
			    GROUP BY t
			    ORDER BY COUNT(DISTINCT pt.id) + COUNT(DISTINCT uft.id) DESC
			""")
	List<Object[]> findMostPlayedTracks(Pageable pageable);
	

	@Query("""
			    SELECT t,
			           COUNT(uft.id)
			    FROM Track t
			    LEFT JOIN UserFavoriteTrack uft
			           ON uft.track = t
			    GROUP BY t
			    ORDER BY COUNT(uft.id) DESC
			""")
	List<Object[]> findMostFavoritedTracks(Pageable pageable);

}