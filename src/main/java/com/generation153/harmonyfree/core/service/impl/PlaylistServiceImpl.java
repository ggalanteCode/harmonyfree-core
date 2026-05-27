package com.generation153.harmonyfree.core.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.generation153.harmonyfree.core.dto.CreatePlaylistRequest;
import com.generation153.harmonyfree.core.dto.PlaylistResponse;
import com.generation153.harmonyfree.core.dto.TrackSearchResponse;
import com.generation153.harmonyfree.core.dto.UpdatePlaylistRequest;
import com.generation153.harmonyfree.core.entity.Playlist;
import com.generation153.harmonyfree.core.entity.Track;
import com.generation153.harmonyfree.core.entity.User;
import com.generation153.harmonyfree.core.repository.PlaylistRepository;
import com.generation153.harmonyfree.core.repository.UserRepository;
import com.generation153.harmonyfree.core.service.PlaylistService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;

    
    //  POST - CREATE PLAYLIST
    

    @Override
    public PlaylistResponse createPlaylist(CreatePlaylistRequest request) {

        //  VALIDAZIONE
        if (request.getUserId() == null) {
            throw new RuntimeException("UserId is required");
        }

        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new RuntimeException("Title is required");
        }

        //  RECUPERO UTENTE
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        //  CREAZIONE ENTITY
        Playlist playlist = new Playlist();
        playlist.setUser(user);
        playlist.setTitle(request.getTitle());
        playlist.setDescription(request.getDescription());
        playlist.setCreatedAt(LocalDateTime.now());
        playlist.setUpdatedAt(LocalDateTime.now());

        //  LISTA VUOTA
        playlist.setPlaylistTracks(new ArrayList<>());

        //  SAVE
        Playlist saved = playlistRepository.save(playlist);

        //  MAPPING
        return mapToPlaylistResponse(saved);
    }

    
    //  GET - PLAYLIST BY ID
    

    @Override
    public PlaylistResponse getPlaylistById(Long id) {

        Playlist playlist = playlistRepository.findByIdWithTracks(id)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        List<TrackSearchResponse> tracks = playlist.getPlaylistTracks().stream()
                .map(pt -> mapToTrackResponse(pt.getTrack()))
                .toList();

        return new PlaylistResponse(
                playlist.getId(),
                playlist.getTitle(),
                playlist.getDescription(),
                playlist.getCreatedAt(),
                tracks
        );
    }

    
    //  MAPPING PLAYLIST
    

    private PlaylistResponse mapToPlaylistResponse(Playlist playlist) {
        return new PlaylistResponse(
                playlist.getId(),
                playlist.getTitle(),
                playlist.getDescription(),
                playlist.getCreatedAt(),
                List.of()
        );
    }

    
    //  MAPPING TRACK
    

    private TrackSearchResponse mapToTrackResponse(Track track) {
        return new TrackSearchResponse(
                track.getId(),
                track.getTitle(),
                track.getArtistName(),
                track.getAlbumName(),
                null
                
        );
    }
 // PUT - UPDATE PLAYLIST

    @Override
    public PlaylistResponse updatePlaylist(Long id, UpdatePlaylistRequest request) {

        //  VALIDAZIONE
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new RuntimeException("Title is required");
        }

        //  RECUPERO PLAYLIST
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        //  UPDATE CAMPI
        playlist.setTitle(request.getTitle());
        playlist.setDescription(request.getDescription());

        //  UPDATE TIMESTAMP
        playlist.setUpdatedAt(LocalDateTime.now());

        //  SAVE
        Playlist updatedPlaylist = playlistRepository.save(playlist);

        //  RESPONSE
        return new PlaylistResponse(
                updatedPlaylist.getId(),
                updatedPlaylist.getTitle(),
                updatedPlaylist.getDescription(),
                updatedPlaylist.getCreatedAt(),
                List.of()
        );
    }
 // PATCH - PARTIAL UPDATE PLAYLIST

    @Override
    public PlaylistResponse patchPlaylist(Long id, UpdatePlaylistRequest request) {

        // RECUPERO PLAYLIST
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        // VALIDAZIONE MINIMA
        if (request.getTitle() == null && request.getDescription() == null) {
            throw new RuntimeException("At least one field must be provided");
        }

        // UPDATE PARZIALE
        if (request.getTitle() != null) {

            if (request.getTitle().isBlank()) {
                throw new RuntimeException("Title cannot be blank");
            }

            playlist.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            playlist.setDescription(request.getDescription());
        }

        // UPDATE TIMESTAMP
        playlist.setUpdatedAt(LocalDateTime.now());

        // SAVE
        Playlist updatedPlaylist = playlistRepository.save(playlist);

        // RESPONSE
        return new PlaylistResponse(
                updatedPlaylist.getId(),
                updatedPlaylist.getTitle(),
                updatedPlaylist.getDescription(),
                updatedPlaylist.getCreatedAt(),
                List.of()
        );
    }
 // DELETE - PLAYLIST

    @Override
    public void deletePlaylist(Long id) {

        // RECUPERO PLAYLIST
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        // DELETE PLAYLIST
        playlistRepository.delete(playlist);
    }
 // GET - PLAYLIST TRACKS

    @Override
    public List<TrackSearchResponse> getPlaylistTracks(Long playlistId) {

        // RECUPERO PLAYLIST + TRACKS
        Playlist playlist = playlistRepository.findByIdWithTracks(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        // MAPPING TRACKS
        return playlist.getPlaylistTracks().stream()
                .map(pt -> mapToTrackResponse(pt.getTrack()))
                .toList();
    }
}