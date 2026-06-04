package com.generation153.harmonyfree.core.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.generation153.harmonyfree.core.client.JamendoClient;
import com.generation153.harmonyfree.core.dto.jamendo.JamendoSearchResponse;
import com.generation153.harmonyfree.core.dto.jamendo.JamendoTrackDto;
import com.generation153.harmonyfree.core.dto.jamendo.JamendoTrackResponse;
import com.generation153.harmonyfree.core.dto.playlist.CreatePlaylistRequest;
import com.generation153.harmonyfree.core.dto.playlist.PlaylistResponse;
import com.generation153.harmonyfree.core.dto.playlist.UpdatePlaylistRequest;
import com.generation153.harmonyfree.core.dto.track.AddTrackRequest;
import com.generation153.harmonyfree.core.dto.track.TrackResponse;
import com.generation153.harmonyfree.core.dto.track.TrackSearchRequest;
import com.generation153.harmonyfree.core.entity.Playlist;
import com.generation153.harmonyfree.core.entity.PlaylistTrack;
import com.generation153.harmonyfree.core.entity.Track;
import com.generation153.harmonyfree.core.entity.User;
import com.generation153.harmonyfree.core.repository.PlaylistRepository;
import com.generation153.harmonyfree.core.repository.TrackRepository;
import com.generation153.harmonyfree.core.repository.UserRepository;
import com.generation153.harmonyfree.core.security.model.CustomUserPrincipal;
import com.generation153.harmonyfree.core.service.PlaylistService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistRepository playlistRepository;

    private final UserRepository userRepository;

    private final TrackRepository trackRepository;

    private final JamendoClient jamendoClient;
    
    // POST - CREATE PLAYLIST
    
    @Override
    public PlaylistResponse createPlaylist(CreatePlaylistRequest request) {

    	CustomUserPrincipal principal = getCustomUserPrincipal();
		Integer authUserId = principal.getUserId();

        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new RuntimeException("Title is required");
        }

        // RECUPERO UTENTE
        User user = userRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // CREAZIONE PLAYLIST
        Playlist playlist = new Playlist();

        playlist.setUser(user);

        playlist.setTitle(request.getTitle());

        playlist.setDescription(request.getDescription());
        
        playlist.setIsPublic(request.getIsPublic());

        playlist.setCreatedAt(LocalDateTime.now());

        playlist.setUpdatedAt(LocalDateTime.now());

        playlist.setPlaylistTracks(new ArrayList<>());

        // SAVE
        Playlist savedPlaylist = playlistRepository.save(playlist);

        // RESPONSE
        return mapToPlaylistResponse(savedPlaylist);
    }

   
    // GET - PLAYLIST BY ID
   

    @Override
    public PlaylistResponse getPlaylistById(Long id) {

        Playlist playlist = playlistRepository.findByIdWithTracks(id)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        return mapToPlaylistResponseWithTracks(playlist);
    }

    @Override
    public List<PlaylistResponse> getMyPlaylists() {
    	
    	CustomUserPrincipal customUserPrincipal = getCustomUserPrincipal();
    	Integer authUserId = customUserPrincipal.getUserId();
    	
    	User userWithPlaylists = userRepository.findByAuthUserIdWithPlaylists(authUserId)
    			.orElseThrow(() -> new RuntimeException("User not found"));
    	
    	List<Playlist> myPlaylists = userWithPlaylists.getPlaylists();
    	
    	return myPlaylists
    			.stream()
    			.map(playlist -> mapToPlaylistResponse(playlist))
    			.toList();
    }
    
    // PUT - UPDATE PLAYLIST
    

    @Override
    public PlaylistResponse updatePlaylist(Long id, UpdatePlaylistRequest request) {
    	
    	//VERIFICA CHE 

        // VALIDAZIONE
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new RuntimeException("Title is required");
        }

        // RECUPERO PLAYLIST
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        // UPDATE CAMPI
        playlist.setTitle(request.getTitle());

        playlist.setDescription(request.getDescription());

        playlist.setUpdatedAt(LocalDateTime.now());

        // SAVE
        Playlist updatedPlaylist = playlistRepository.save(playlist);

        // RESPONSE
        return mapToPlaylistResponse(updatedPlaylist);
    }

    
    // PATCH - PLAYLIST
   

    @Override
    public PlaylistResponse patchPlaylist(Long id, UpdatePlaylistRequest request) {

        // RECUPERO PLAYLIST
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        // VALIDAZIONE
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
        return mapToPlaylistResponse(updatedPlaylist);
    }

    
    // DELETE - PLAYLIST
   

    @Override
    public void deletePlaylist(Long id) {

        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        playlistRepository.delete(playlist);
    }

    
    // 🔹 GET - PLAYLIST TRACKS
   

    @Override
    public List<TrackResponse> getPlaylistTracks(Long playlistId) {

        Playlist playlist = playlistRepository.findByIdWithTracks(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        return playlist.getPlaylistTracks()
                .stream()
                .map(pt -> mapToTrackResponse(pt.getTrack()))
                .toList();
    }

    
    // POST - ADD TRACK TO PLAYLIST
    

    @Override
    public PlaylistResponse addTrackToPlaylist(Long playlistId, AddTrackRequest request) {
    	
    	log.info("Playlist id: {}", playlistId);
    	log.info("Jamendo track id: {}", request.getJamendoTrackId());

        // VALIDAZIONE
        if (request.getJamendoTrackId() == null) {
            throw new RuntimeException("jamendoTrackId is required");
        }

        // RECUPERO PLAYLIST
        Playlist playlist = playlistRepository.findByIdWithTracks(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        // CONTROLLO DUPLICATI
        boolean alreadyExists = playlist.getPlaylistTracks()
                .stream()
                .anyMatch(pt -> pt.getTrack()
                        .getJamendoTrackId()
                        .equals(request.getJamendoTrackId()));

        if (alreadyExists) {
            throw new RuntimeException("Track already in playlist");
        }

        // CERCA TRACK NEL DB
        Optional<Track> existingTrack =
                trackRepository.findByJamendoTrackId(request.getJamendoTrackId());

        Track track;

        
        // TRACK GIÀ PRESENTE
        

        if (existingTrack.isPresent()) {

            track = existingTrack.get();

        } else {

            // TRACK NON PRESENTE → JAMENDO

            JamendoTrackResponse response = jamendoClient.getTrackById(request.getJamendoTrackId());

            // TRACK NON TROVATA
            if (response == null || response.getResults().isEmpty()) {
                throw new RuntimeException("Track not found on Jamendo");
            }

            // DTO JAMENDO
            JamendoTrackDto dto = response.getResults().get(0);

            // CREAZIONE TRACK
            track = new Track();

            track.setJamendoTrackId(dto.getId());

            track.setJamendoArtistId(dto.getArtistId());

            track.setJamendoAlbumId(dto.getAlbumId());

            track.setTitle(dto.getName());

            track.setArtistName(dto.getArtistName());

            track.setAlbumName(dto.getAlbumName());

            track.setAudioUrl(dto.getAudioUrl());

            track.setCoverUrl(dto.getCoverUrl());

            track.setCreatedAt(LocalDateTime.now());
            
            track.setDownloadable(dto.getDownloadable());
            
            track.setDownloadUrl(dto.getDownloadUrl());
            
            track.setDuration(dto.getDuration());

            // SAVE TRACK
            track = trackRepository.save(track);
        }

        
        // CREAZIONE PLAYLIST TRACK
       

        PlaylistTrack playlistTrack = new PlaylistTrack();

        playlistTrack.setPlaylist(playlist);

        playlistTrack.setTrack(track);

        playlistTrack.setPosition(
                playlist.getPlaylistTracks().size() + 1
        );

        playlistTrack.setCreatedAt(LocalDateTime.now());

        // 🔹 ADD RELAZIONE
        playlist.getPlaylistTracks().add(playlistTrack);

        // 🔹 SAVE PLAYLIST
        Playlist updatedPlaylist = playlistRepository.save(playlist);
        
        log.info("Tracks in playlist: {}", updatedPlaylist.getPlaylistTracks().size());

        // 🔹 RESPONSE
        return mapToPlaylistResponseWithTracks(updatedPlaylist);
    }

    
    // MAPPING PLAYLIST RESPONSE
   

    private PlaylistResponse mapToPlaylistResponse(Playlist playlist) {

        return new PlaylistResponse(
                playlist.getId(),
                playlist.getTitle(),
                playlist.getDescription(),
                playlist.getIsPublic(),
                playlist.getCreatedAt(),
                List.of()
        );
    }

    
    // MAPPING TRACK RESPONSE
   

    private TrackResponse mapToTrackResponse(Track track) {

        TrackResponse response = new TrackResponse();

        response.setId(track.getId());

        response.setJamendoTrackId(track.getJamendoTrackId());

        response.setTitle(track.getTitle());

        response.setArtist(track.getArtistName());

        response.setAlbum(track.getAlbumName());

        response.setGenres(
                track.getTrackGenres()
                        .stream()
                        .map(tg -> tg.getGenre().getName())
                        .toList()
        );

        response.setCoverImageUrl(track.getCoverUrl());

        response.setAudioUrl(track.getAudioUrl());

        response.setCreatedAt(track.getCreatedAt());
        
        response.setDownloadUrl(track.getDownloadUrl());
        
        response.setDownloadable(track.getDownloadable());

        return response;
    }

    
    // PLAYLIST RESPONSE WITH TRACKS
  

    private PlaylistResponse mapToPlaylistResponseWithTracks(Playlist playlist) {

        List<TrackResponse> tracks = playlist.getPlaylistTracks()
                .stream()
                .map(pt -> mapToTrackResponse(pt.getTrack()))
                .toList();

        return new PlaylistResponse(
                playlist.getId(),
                playlist.getTitle(),
                playlist.getDescription(),
                playlist.getIsPublic(),
                playlist.getCreatedAt(),
                tracks
        );
    }
 
 // DELETE - REMOVE TRACK FROM PLAYLIST


 @Override
 public void removeTrackFromPlaylist(Long playlistId, Long trackId) {

     // RECUPERO PLAYLIST + TRACKS
     Playlist playlist = playlistRepository.findByIdWithTracks(playlistId)
             .orElseThrow(() -> new RuntimeException("Playlist not found"));

     // CERCA PLAYLIST TRACK
     PlaylistTrack playlistTrack = playlist.getPlaylistTracks()
             .stream()
             .filter(pt -> pt.getTrack().getId().equals(trackId))
             .findFirst()
             .orElseThrow(() ->
                     new RuntimeException("Track not found in playlist"));

     // REMOVE RELAZIONE
     playlist.getPlaylistTracks().remove(playlistTrack);

     // SAVE PLAYLIST
     playlistRepository.save(playlist);

   
     // CHECK TRACK ORFANO
   

     boolean existsInPlaylists =
             playlistRepository.existsByPlaylistTracks_Track_Id(trackId);

     boolean existsInFavorites =
             userRepository.existsByFavoriteTracks_Track_Id(trackId);      

     // DELETE TRACK SE ORFANO
     if (!existsInPlaylists && !existsInFavorites) {

         trackRepository.deleteById(trackId);
     }
 }
 
 private CustomUserPrincipal getCustomUserPrincipal() {
	return (CustomUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
 }
 
}