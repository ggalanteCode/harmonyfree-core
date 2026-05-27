package com.generation153.harmonyfree.core.contoller;

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

import com.generation153.harmonyfree.core.dto.AddTrackRequest;
import com.generation153.harmonyfree.core.dto.CreateUserRequest;
import com.generation153.harmonyfree.core.dto.PatchUserRequest;
import com.generation153.harmonyfree.core.dto.PlaylistResponse;
import com.generation153.harmonyfree.core.dto.TrackResponse;
import com.generation153.harmonyfree.core.dto.UserResponse;
import com.generation153.harmonyfree.core.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController<UpdateUserRequest> {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;

	}

	@PostMapping
	public UserResponse createUser(@RequestBody CreateUserRequest request) {
		return userService.createUser(request);
	}

	@GetMapping("/{id}")
	public UserResponse getUser(@PathVariable Long id) {
		return userService.getUserById(id);

	}

	@PutMapping("/{id}")
	public UserResponse updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
		return userService.updateUser(id, request);
	}

	@PatchMapping("/{id}")
	public UserResponse patchUser(@PathVariable Long id, @RequestBody PatchUserRequest request) {
		return userService.patchUser(id, request);
	}

	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
	}

	@PostMapping("/{userId}/favorites")
	public List<TrackResponse> addFavorite(@PathVariable Long userId, @RequestBody AddTrackRequest request) {
		return userService.addFavorite(userId, request);
	}

	@GetMapping("/{userId}/favorites")
	public List<TrackResponse> getFavorites(@PathVariable Long userId) {
		return userService.getUserFavorites(userId);
	}

	@DeleteMapping("/{userId}/favorites/{trackId}")
	public void removeFavorite(@PathVariable Long userId, @PathVariable Long trackId) {
		userService.removeFavorite(userId, trackId);
	}

	@GetMapping("/{userId}/playlists")
	public List<PlaylistResponse> getUserPlaylists(@PathVariable Long userId) {
		return userService.getUserPlaylists(userId);

	}

}