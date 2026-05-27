package com.generation153.harmonyfree.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation153.harmonyfree.core.entity.Track;

public interface TrackRepository extends JpaRepository<Track, Long> {

	Optional<Track> findByJamendoTrackId(Long jamendoTrackId);

}