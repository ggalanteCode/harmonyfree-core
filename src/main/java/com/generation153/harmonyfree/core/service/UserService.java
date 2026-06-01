package com.generation153.harmonyfree.core.service;

import java.util.List;

import com.generation153.harmonyfree.core.dto.playlist.PlaylistResponse;
import com.generation153.harmonyfree.core.dto.track.AddTrackRequest;
import com.generation153.harmonyfree.core.dto.track.TrackResponse;
import com.generation153.harmonyfree.core.dto.user.CreateUserRequest;
import com.generation153.harmonyfree.core.dto.user.PatchUserRequest;
import com.generation153.harmonyfree.core.dto.user.UpdateUserRequest;
import com.generation153.harmonyfree.core.dto.user.UserResponse;

public interface UserService {

	UserResponse getUserById(Long id);

	UserResponse updateUser(Long id, UpdateUserRequest request);

	void deleteUser(Long id);

	List<TrackResponse> getUserFavorites(Long userId);

	void removeFavorite(Long userId, Long trackId);

	List<PlaylistResponse> getUserPlaylists(Long userId);

	UserResponse createUser(CreateUserRequest request);

	UserResponse patchUser(Long id, PatchUserRequest request);

	List<TrackResponse> addFavorite(Long userId, AddTrackRequest request);
	
}
