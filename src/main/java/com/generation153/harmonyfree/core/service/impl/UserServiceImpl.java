package com.generation153.harmonyfree.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.generation153.harmonyfree.core.dto.AddTrackRequest;
import com.generation153.harmonyfree.core.dto.CreateUserRequest;
import com.generation153.harmonyfree.core.dto.PatchUserRequest;
import com.generation153.harmonyfree.core.dto.PlaylistResponse;
import com.generation153.harmonyfree.core.dto.TrackResponse;
import com.generation153.harmonyfree.core.dto.UpdateUserRequest;
import com.generation153.harmonyfree.core.dto.UserResponse;
import com.generation153.harmonyfree.core.entity.User;
import com.generation153.harmonyfree.core.exception.ResourceNotFoundException;
import com.generation153.harmonyfree.core.repository.UserRepository;
import com.generation153.harmonyfree.core.security.model.CustomUserPrincipal;
import com.generation153.harmonyfree.core.service.UserService;
import java.time.LocalDateTime;
import java.util.Optional;

import com.generation153.harmonyfree.core.client.JamendoClient;
import com.generation153.harmonyfree.core.dto.JamendoSearchResponse;
import com.generation153.harmonyfree.core.dto.JamendoTrackDto;
import com.generation153.harmonyfree.core.dto.TrackSearchRequest;
import com.generation153.harmonyfree.core.entity.Track;
import com.generation153.harmonyfree.core.entity.UserFavoriteTrack;
import com.generation153.harmonyfree.core.repository.PlaylistRepository;
import com.generation153.harmonyfree.core.repository.TrackRepository;

@Service
public class UserServiceImpl implements UserService {

	// INJECTION REPOSITORY
	public final UserRepository userRepository;
	public final TrackRepository trackRepository;
	public final JamendoClient jamendoClient;
	public final PlaylistRepository playlistRepository;

	public UserServiceImpl(
	        UserRepository userRepository,
	        TrackRepository trackRepository,
	        PlaylistRepository playlistRepository,
	        JamendoClient jamendoClient) {

	    this.userRepository = userRepository;
	    this.trackRepository = trackRepository;
	    this.playlistRepository = playlistRepository;
	    this.jamendoClient = jamendoClient;
	}

	// GET USER BY ID (CREAZIONE UTENTE NEL DATABASE) DA COMPLETARE!!
	@Override
	public UserResponse getUserById(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User non trovato"));

		return mapToResponse(user);
	}

	@Override
	public UserResponse updateUser(Long id, UpdateUserRequest request) {
		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User non trovato"));
		user.setUsername(request.getUsername());
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setEmail(request.getEmail());
		User saved = userRepository.save(user);

		return mapToResponse(saved);

	}

	// CANCELLA USER
	@Override
	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}

	// TRACCE FAVORITE USER
	@Override
	public List<TrackResponse> getUserFavorites(Long userId) {

	    User user = userRepository.findByIdWithFavorites(userId)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException("User not found"));

	    return user.getFavoriteTracks()
	            .stream()
	            .map(favorite ->
	                    mapToTrackResponse(favorite.getTrack()))
	            .toList();
	}


	// PLAYLIST USER
	@Override
	public List<PlaylistResponse> getUserPlaylists(Long userId) {
		return new ArrayList<>();
	}

	// CREAZIONE USER
	@Override
	public UserResponse createUser(CreateUserRequest request) {
		
		CustomUserPrincipal principal = (CustomUserPrincipal)
			    SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		//RECUPERA L'authUserId E L'email SEMPRE DAL TOKEN
		Long authUserId = principal.getUserId();
		String email = principal.getEmail();
		
		//VERIFICA SE L'UTENTE CON  QUELL'authUserId ESISTE GIA', IN TAL CASO 
		//LANCIA UN'ECCEZIONE
		if (userRepository.existsByAuthUserId(authUserId)) {
		    throw new RuntimeException("User già esistente");
		}
		
		User user = new User();
		user.setAuthUserId(authUserId);
		user.setEmail(email);
		user.setUsername(request.getUsername());
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setCreatedAt(LocalDateTime.now());
		user.setProfileImageUrl(request.getProfileImageUrl());
		
		User saved = userRepository.save(user);
		return mapToResponse(saved);
		
	}
	
	// PATCH USER
	@Override
	public UserResponse patchUser(Long id, PatchUserRequest request) {
		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User non trovato"));

		if (request.getUsername() != null) {
			user.setUsername(request.getUsername());
		}
		if (request.getFirstName() != null) {
			user.setFirstName(request.getFirstName());
		}
		if (request.getLastName() != null) {
			user.setLastName(request.getLastName());
		}
		if (request.getEmail() != null) {
			user.setEmail(request.getEmail());
		}
		if (request.getProfileImageUrl() != null) {
			user.setProfileImageUrl(request.getProfileImageUrl());
		}
		return mapToResponse(userRepository.save(user));
	}
    
	// AGGIUNGE BRANO AI PREFERITI
	@Override
	public List<TrackResponse> addFavorite(Long userId, AddTrackRequest request) {

	    if (request.getJamendoTrackId() == null) {
	        throw new RuntimeException("jamendoTrackId is required");
	    }

	    User user = userRepository.findByIdWithFavorites(userId)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException("User not found"));

	    boolean alreadyExists = user.getFavoriteTracks()
	            .stream()
	            .anyMatch(f ->
	                    f.getTrack()
	                     .getJamendoTrackId()
	                     .equals(request.getJamendoTrackId()));

	    if (alreadyExists) {
	        throw new RuntimeException("Track already in favorites");
	    }

	    Optional<Track> existingTrack =
	            trackRepository.findByJamendoTrackId(
	                    request.getJamendoTrackId());

	    Track track;

	    if (existingTrack.isPresent()) {

	        track = existingTrack.get();

	    } else {

	        TrackSearchRequest searchRequest =
	                new TrackSearchRequest();

	        searchRequest.setQuery(
	                request.getJamendoTrackId().toString());

	        searchRequest.setLimit(1);

	        JamendoSearchResponse response =
	                jamendoClient.searchTracks(searchRequest);

	        if (response == null ||
	                response.getResults().isEmpty()) {

	            throw new RuntimeException(
	                    "Track not found on Jamendo");
	        }

	        JamendoTrackDto dto =
	                response.getResults().get(0);
	        
	     // CREAZIONE TRACK

	        track = new Track();

	        track.setJamendoTrackId(dto.getId());

	        track.setJamendoArtistId(dto.getArtistId());

	        track.setJamendoAlbumId(dto.getAlbumId());

	        track.setTitle(dto.getName());

	        track.setArtistName(dto.getArtistName());

	        track.setAlbumName(dto.getAlbumName());

	        track.setDuration(dto.getDuration());

	        track.setAudioUrl(dto.getAudioUrl());

	        track.setCoverUrl(dto.getCoverUrl());

	        track.setCreatedAt(LocalDateTime.now());

	        track = trackRepository.save(track);
	    }
	    
	 // CREAZIONE RELAZIONE USER - TRACK

	    UserFavoriteTrack favorite =
	            new UserFavoriteTrack();

	    favorite.setUser(user);

	    favorite.setTrack(track);

	    favorite.setCreatedAt(LocalDateTime.now());

	    user.getFavoriteTracks().add(favorite);

	    User updatedUser =
	            userRepository.save(user);

	    return updatedUser.getFavoriteTracks()
	            .stream()
	            .map(f -> mapToTrackResponse(
	                    f.getTrack()))
	            .toList();
	}
	
	// MAPPING USER RESPONSE

	private UserResponse mapToResponse(User user) {
		UserResponse res = new UserResponse();
		res.setId(user.getId());
		res.setUsername(user.getUsername());
		res.setFirstName(user.getFirstName());
		res.setLastName(user.getLastName());
		res.setEmail(user.getEmail());
		res.setProfileImageUrl(user.getProfileImageUrl());
		return res;
	}
	
	// MAPPING TRACK RESPONSE
	private TrackResponse mapToTrackResponse(Track track) {

	    TrackResponse response =
	            new TrackResponse();

	    response.setId(track.getId());

	    response.setJamendoTrackId(
	            track.getJamendoTrackId());

	    response.setTitle(track.getTitle());

	    response.setArtist(
	            track.getArtistName());

	    response.setAlbum(
	            track.getAlbumName());

	    response.setGenres(
	            track.getTrackGenres()
	                    .stream()
	                    .map(tg -> tg.getGenre().getName())
	                    .toList()
	    );

	    response.setDuration(
	            track.getDuration());

	    response.setCoverImageUrl(
	            track.getCoverUrl());

	    response.setAudioUrl(
	            track.getAudioUrl());

	    response.setCreatedAt(
	            track.getCreatedAt());

	    return response;
	}
	// RIMUOVE BRANO DAI PREFERITI
	@Override
	public void removeFavorite(Long userId, Long trackId) {

	    // RECUPERO USER
	    User user = userRepository.findByIdWithFavorites(userId)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException("User not found"));

	    // CERCA RELAZIONE USER - TRACK
	    UserFavoriteTrack favorite =
	            user.getFavoriteTracks()
	                    .stream()
	                    .filter(f ->
	                            f.getTrack()
	                             .getId()
	                             .equals(trackId))
	                    .findFirst()
	                    .orElseThrow(() ->
	                            new ResourceNotFoundException(
	                                    "Track not found in favorites"));

	    // RIMOZIONE RELAZIONE
	    user.getFavoriteTracks().remove(favorite);

	    // SAVE USER
	    userRepository.save(user);

	    // TRACK USATA IN ALTRE PLAYLIST?
	    boolean existsInPlaylists =
	            playlistRepository
	                    .existsByPlaylistTracks_Track_Id(trackId);

	    // TRACK USATA IN ALTRI FAVORITES?
	    boolean existsInFavorites =
	            userRepository
	                    .existsByFavoriteTracks_Track_Id(trackId);

	    // ELIMINA TRACK ORFANA
	    if (!existsInPlaylists && !existsInFavorites) {

	        trackRepository.deleteById(trackId);
	    }
	}

}
