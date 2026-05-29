package com.generation153.harmonyfree.core.service;

import java.util.List;

//import com.generation153.harmonyfree.core.contoller.PlaylistResponse;
import com.generation153.harmonyfree.core.dto.AddTrackRequest;
import com.generation153.harmonyfree.core.dto.CreateUserRequest;
import com.generation153.harmonyfree.core.dto.PatchUserRequest;
import com.generation153.harmonyfree.core.dto.PlaylistResponse;
import com.generation153.harmonyfree.core.dto.TrackResponse;
import com.generation153.harmonyfree.core.dto.UpdateUserRequest;
import com.generation153.harmonyfree.core.dto.UserResponse;

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
