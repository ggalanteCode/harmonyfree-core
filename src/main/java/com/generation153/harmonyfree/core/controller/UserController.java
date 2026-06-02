package com.generation153.harmonyfree.core.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation153.harmonyfree.core.dto.playlist.PlaylistResponse;
import com.generation153.harmonyfree.core.dto.track.AddTrackRequest;
import com.generation153.harmonyfree.core.dto.track.TrackResponse;
import com.generation153.harmonyfree.core.dto.user.CreateUserRequest;
import com.generation153.harmonyfree.core.dto.user.PatchUserRequest;
import com.generation153.harmonyfree.core.dto.user.UpdateUserRequest;
import com.generation153.harmonyfree.core.dto.user.UserResponse;
import com.generation153.harmonyfree.core.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;

	}
	
	//NON CI SONO CHIAMATE PUBBLICHE IN USERCONTROLLER
	//SOLO USER E/O ADMIN

	//Creazione profilo (una sola volta dopo il login)
	//POSSONO FARLA SIA USER CHE ADMIN, NO PUBBLICA
	@PostMapping
	public UserResponse createUser(@RequestBody CreateUserRequest request) {
		return userService.createUser(request);
	}
	
	//SEZIONE PROFILO UTENTE AUTENTICATO
	
	@GetMapping("/me")
    public UserResponse getMe() {
        return userService.getCurrentUser();
    }

	@PutMapping("/me")
	public UserResponse updateMe(@RequestBody UpdateUserRequest request) {
		return userService.updateCurrentUser(request);
	}

	@PatchMapping("/me")
	public UserResponse patchMe(@RequestBody PatchUserRequest request) {
		return userService.patchCurrentUser(request);
	}
	
	@DeleteMapping("/me")
    public void deleteMe() {
		userService.deleteCurrentUser();
    }
	
	//SEZIONE FAVORITES

	@PostMapping("/me/favorites")
	public List<TrackResponse> addFavorite(@RequestBody AddTrackRequest request) {
		return userService.addFavoriteToCurrentUser(request);
	}

	@GetMapping("/me/favorites")
	public List<TrackResponse> getFavorites() {
		return userService.getCurrentUserFavorites();
	}

	@DeleteMapping("/me/favorites/{trackId}")
	public void removeFavorite(@PathVariable Long trackId) {
		userService.removeFavoriteFromCurrentUser(trackId);
	}
	
	//SEZIONE PLAYLISTS DELL’UTENTE

	@GetMapping("/me/playlists")
	public List<PlaylistResponse> getMyPlaylists() {
		return userService.getCurrentUserPlaylists();
	}
	
	//SEZIONE ADMIN ONLY (opzionale)
	
	@GetMapping("/{id}")
	public UserResponse getUserById(@PathVariable Long id) {
		return userService.getUserById(id);
	}
	
	@DeleteMapping("/{id}")
	public void deleteUserById(@PathVariable Long id) {
		userService.deleteUser(id);
	}

}