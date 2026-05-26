package com.generation153.harmonyfree.core.repository;

import com.generation153.harmonyfree.core.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    //per utente 
    List<Playlist> findByUserId(Long userId);

    //pubbliche
    List<Playlist> findByIsPublicTrue();

   //pubbliche per utente
    List<Playlist> findByUserIdAndIsPublicTrue(Long userId);

   //ricerca titolo
    List<Playlist> findByTitleContainingIgnoreCase(String title);
    @Query("""
    	    SELECT p FROM Playlist p
    	    LEFT JOIN FETCH p.playlistTracks pt
    	    LEFT JOIN FETCH pt.track
    	    WHERE p.id = :id
    	""")
    	Optional<Playlist> findByIdWithTracks(Long id);
}