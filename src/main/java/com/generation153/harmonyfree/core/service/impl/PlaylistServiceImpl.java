package com.generation153.harmonyfree.core.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.generation153.harmonyfree.core.dto.CreatePlaylistRequest;
import com.generation153.harmonyfree.core.dto.PlaylistResponse;
import com.generation153.harmonyfree.core.dto.TrackSearchResponse;
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

    // =====================================================
    // 🔹 POST - CREATE PLAYLIST
    // =====================================================

    @Override
    public PlaylistResponse createPlaylist(CreatePlaylistRequest request) {

        // 🔹 VALIDAZIONE
        if (request.getUserId() == null) {
            throw new RuntimeException("UserId is required");
        }

        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new RuntimeException("Title is required");
        }

        // 🔹 RECUPERO UTENTE
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔹 CREAZIONE ENTITY
        Playlist playlist = new Playlist();
        playlist.setUser(user);
        playlist.setTitle(request.getTitle());
        playlist.setDescription(request.getDescription());
        playlist.setCreatedAt(LocalDateTime.now());
        playlist.setUpdatedAt(LocalDateTime.now());

        // 🔹 LISTA VUOTA
        playlist.setPlaylistTracks(new ArrayList<>());

        // 🔹 SAVE
        Playlist saved = playlistRepository.save(playlist);

        // 🔹 MAPPING
        return mapToPlaylistResponse(saved);
    }

    // =====================================================
    // 🔹 GET - PLAYLIST BY ID
    // =====================================================

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

    // =====================================================
    // 🔁 MAPPING PLAYLIST
    // =====================================================

    private PlaylistResponse mapToPlaylistResponse(Playlist playlist) {
        return new PlaylistResponse(
                playlist.getId(),
                playlist.getTitle(),
                playlist.getDescription(),
                playlist.getCreatedAt(),
                List.of()
        );
    }

    // =====================================================
    // 🔁 MAPPING TRACK
    // =====================================================

    private TrackSearchResponse mapToTrackResponse(Track track) {
        return new TrackSearchResponse(
                track.getId(),
                track.getTitle(),
                track.getArtistName(),
                track.getAlbumName()
        );
    }
}