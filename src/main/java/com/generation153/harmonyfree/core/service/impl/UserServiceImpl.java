package com.generation153.harmonyfree.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.generation153.harmonyfree.core.dto.jamendo.JamendoTrackDto;
import com.generation153.harmonyfree.core.dto.jamendo.JamendoTrackResponse;
import com.generation153.harmonyfree.core.dto.playlist.PlaylistResponse;
import com.generation153.harmonyfree.core.dto.track.AddTrackRequest;
import com.generation153.harmonyfree.core.dto.track.TrackResponse;
import com.generation153.harmonyfree.core.dto.user.CreateUserRequest;
import com.generation153.harmonyfree.core.dto.user.PatchUserRequest;
import com.generation153.harmonyfree.core.dto.user.UpdateUserRequest;
import com.generation153.harmonyfree.core.dto.user.UserResponse;
import com.generation153.harmonyfree.core.entity.User;
import com.generation153.harmonyfree.core.exception.ResourceNotFoundException;
import com.generation153.harmonyfree.core.repository.UserRepository;
import com.generation153.harmonyfree.core.security.model.CustomUserPrincipal;
import com.generation153.harmonyfree.core.service.UserService;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import com.generation153.harmonyfree.core.client.JamendoClient;
import com.generation153.harmonyfree.core.entity.Genre;
import com.generation153.harmonyfree.core.entity.Playlist;
import com.generation153.harmonyfree.core.entity.Track;
import com.generation153.harmonyfree.core.entity.UserFavoriteTrack;
import com.generation153.harmonyfree.core.repository.GenreRepository;
import com.generation153.harmonyfree.core.repository.PlaylistRepository;
import com.generation153.harmonyfree.core.repository.TrackRepository;

@Service
public class UserServiceImpl implements UserService {

	// INJECTION REPOSITORY
	public final UserRepository userRepository;
	public final TrackRepository trackRepository;
	public final JamendoClient jamendoClient;
	public final PlaylistRepository playlistRepository;
	public final GenreRepository genreRepository;

	public UserServiceImpl(
	        UserRepository userRepository,
	        TrackRepository trackRepository,
	        PlaylistRepository playlistRepository,
	        JamendoClient jamendoClient,
	        GenreRepository genreRepository) {

	    this.userRepository = userRepository;
	    this.trackRepository = trackRepository;
	    this.playlistRepository = playlistRepository;
	    this.jamendoClient = jamendoClient;
	    this.genreRepository = genreRepository;
	}
	
	// CREAZIONE USER
	@Override
	public UserResponse createUser(CreateUserRequest request) {
		
		//RECUPERA L'authUserId SEMPRE DAL TOKEN
		CustomUserPrincipal principal = getCustomUserPrincipal();
		Integer authUserId = principal.getUserId();
		
		//VERIFICA SE L'UTENTE CON  QUELL'authUserId ESISTE GIA', IN TAL CASO 
		//LANCIA UN'ECCEZIONE
		if (userRepository.existsByAuthUserId(authUserId)) {
		    throw new RuntimeException("User già esistente");
		}
		
		User user = new User();
		user.setAuthUserId(authUserId);
		user.setUsername(request.getUsername());
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setCreatedAt(LocalDateTime.now());
		user.setProfileImageUrl(null);	//inizialmente, l'utente si registra senza caricare un'immagine del profilo.
		
		User saved = userRepository.save(user);
		
		return mapToUserResponse(saved);
		
	}
	
	@Override
	public UserResponse getCurrentUser() {
		
		CustomUserPrincipal principal = getCustomUserPrincipal();
		Integer authUserId = principal.getUserId();
		
		User userByAuthUserId = userRepository.findByAuthUserId(authUserId)
				.orElseThrow(() -> new ResourceNotFoundException("User corrente non trovato"));
		
		return mapToUserResponse(userByAuthUserId);
	}
	
	@Override
	public UserResponse updateCurrentUser(UpdateUserRequest request) {
		
		CustomUserPrincipal principal = getCustomUserPrincipal();
		Integer authUserId = principal.getUserId();
		
		User userByAuthUserId = userRepository.findByAuthUserId(authUserId)
				.orElseThrow(() -> new ResourceNotFoundException("User corrente non trovato"));
		
		userByAuthUserId.setUsername(request.getUsername());
		userByAuthUserId.setFirstName(request.getFirstName());
		userByAuthUserId.setLastName(request.getLastName());
		userByAuthUserId.setProfileImageUrl(request.getProfileImageUrl());
		userByAuthUserId.setUpdatedAt(LocalDateTime.now());
		
		User saved = userRepository.save(userByAuthUserId);

		return mapToUserResponse(saved);

	}
	
	// PATCH USER
	@Override
	public UserResponse patchCurrentUser(PatchUserRequest request) {
		
		CustomUserPrincipal principal = getCustomUserPrincipal();
		Integer authUserId = principal.getUserId();
		
		User userByAuthUserId = userRepository.findByAuthUserId(authUserId)
				.orElseThrow(() -> new ResourceNotFoundException("User corrente non trovato"));

		if (request.getUsername() != null) {
			userByAuthUserId.setUsername(request.getUsername());
		}
		if (request.getFirstName() != null) {
			userByAuthUserId.setFirstName(request.getFirstName());
		}
		if (request.getLastName() != null) {
			userByAuthUserId.setLastName(request.getLastName());
		}
		if (request.getProfileImageUrl() != null) {
			userByAuthUserId.setProfileImageUrl(request.getProfileImageUrl());
		}
		
		userByAuthUserId.setUpdatedAt(LocalDateTime.now());
		
		return mapToUserResponse(userRepository.save(userByAuthUserId));
	}
	
	@Override
	@Transactional
	public void deleteCurrentUser() {
		
		CustomUserPrincipal principal = getCustomUserPrincipal();
		Integer authUserId = principal.getUserId();
		
		userRepository.deleteByAuthUserId(authUserId);
		
	}
	
	// AGGIUNGE BRANO AI PREFERITI
	@Override
	public List<TrackResponse> addFavoriteToCurrentUser(AddTrackRequest request) {

	    if (request.getJamendoTrackId() == null) {
	        throw new RuntimeException("jamendoTrackId is required");
	    }
	    
	    CustomUserPrincipal principal = getCustomUserPrincipal();
		Integer authUserId = principal.getUserId();

	    User user = userRepository.findByAuthUserIdWithFavorites(authUserId)
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

	        JamendoTrackResponse response = jamendoClient.getTrackById(request.getJamendoTrackId());

	        if (response == null ||
	                response.getResults().isEmpty()) {

	            throw new RuntimeException(
	                    "Track not found on Jamendo");
	        }

	        JamendoTrackDto dto =
	                response.getResults().get(0);
	        
	     // CREAZIONE TRACK

	        track = mapToTrackEntity(dto);

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

	// TRACCE FAVORITE USER
	@Override
	public List<TrackResponse> getCurrentUserFavorites() {
		
		CustomUserPrincipal principal = getCustomUserPrincipal();
		Integer authUserId = principal.getUserId();

	    User user = userRepository.findByAuthUserIdWithFavorites(authUserId)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException("User not found"));

	    return user.getFavoriteTracks()
	            .stream()
	            .map(favorite ->
	                    mapToTrackResponse(favorite.getTrack()))
	            .toList();
	}

	// RIMUOVE BRANO DAI PREFERITI
	@Override
	public void removeFavoriteFromCurrentUser(Long trackId) {
		
		CustomUserPrincipal principal = getCustomUserPrincipal();
		Integer authUserId = principal.getUserId();

	    // RECUPERO USER
	    User user = userRepository.findByAuthUserIdWithFavorites(authUserId)
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

	// PLAYLIST USER
	@Override
	public List<PlaylistResponse> getCurrentUserPlaylists() {
		
		CustomUserPrincipal principal = getCustomUserPrincipal();
		Integer authUserId = principal.getUserId();
		
		User user = userRepository.findByAuthUserIdWithPlaylists(authUserId)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException("User not found"));
		
		return user.getPlaylists()
	            .stream()
	            //RESTITUIAMO SOLO I METADATI DELLE PLAYLIST, SENZA LE LORO TRACKS
	            .map(playlist -> mapToPlaylistResponseWithoutItsTracks(playlist))
	            .toList();
	}
	
	private PlaylistResponse mapToPlaylistResponseWithoutItsTracks(Playlist playlist) {
		PlaylistResponse playlistResponse = new PlaylistResponse();
		playlistResponse.setId(playlist.getId());
		playlistResponse.setTitle(playlist.getTitle());
		playlistResponse.setDescription(playlist.getDescription());
		playlistResponse.setIsPublic(playlist.getIsPublic());
		playlistResponse.setCreatedAt(playlist.getCreatedAt());
		playlistResponse.setTracks(new ArrayList<>());
		return playlistResponse;
	}

	@Override
	public UserResponse getUserById(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User non trovato"));
		return mapToUserResponse(user);
		
	}

	// CANCELLA USER
	@Override
	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}
	
	private List<String> extractGenres(JamendoTrackDto dto) {
		
		if (hasNoGenres(dto)) {
	        return List.of();
	    }

	    return dto.getMusicinfo().getTags().getGenres();
	    
	}
	
	private boolean hasNoGenres(JamendoTrackDto dto) {
		return dto.getMusicinfo() == null 
				|| dto.getMusicinfo().getTags() == null 
				|| dto.getMusicinfo().getTags().getGenres() == null;
	}
	
	//MAPPING TRACK
	
	private Track mapToTrackEntity(JamendoTrackDto dto) {
		Track track = new Track();

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
        
        track.setDownloadUrl(dto.getDownloadUrl());
        
        track.setDownloadable(dto.getDownloadable());
        
        //ESTRAI I NOMI DEI GENERI
        List<String> genreNames = extractGenres(dto);

	    genreNames.forEach(name -> {
	    	
	    	//per non avere generi con nome uguale che distinguono tra maiuscole e minuscole
	    	String normalizedName = name.trim().toLowerCase();
	    	
	        Genre genre = genreRepository.findByName(normalizedName)
	            .orElseGet(() -> genreRepository.save(new Genre(normalizedName)));
	        
	        track.addGenre(genre);
	        
	    });
	    
	    return track;
	}

	// MAPPING USER RESPONSE

	private UserResponse mapToUserResponse(User user) {
		UserResponse res = new UserResponse();
		res.setId(user.getId());
		res.setUsername(user.getUsername());
		res.setFirstName(user.getFirstName());
		res.setLastName(user.getLastName());
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
	    
	    response.setDownloadUrl(track.getDownloadUrl());
	    
	    response.setDownloadable(track.getDownloadable());

	    return response;
	}
	
	private CustomUserPrincipal getCustomUserPrincipal() {
		return (CustomUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

}
