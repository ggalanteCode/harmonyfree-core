package com.generation153.harmonyfree.core.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.generation153.harmonyfree.core.dto.playlist.PlaylistResponse;
import com.generation153.harmonyfree.core.dto.track.AddTrackRequest;
import com.generation153.harmonyfree.core.dto.track.TrackResponse;
import com.generation153.harmonyfree.core.dto.user.CreateUserRequest;
import com.generation153.harmonyfree.core.dto.user.PatchUserRequest;
import com.generation153.harmonyfree.core.dto.user.UpdateUserRequest;
import com.generation153.harmonyfree.core.dto.user.UserResponse;

public interface UserService {
	
	UserResponse createUser(CreateUserRequest request);
	
	UserResponse uploadAvatar(MultipartFile file);
	
	UserResponse getCurrentUser();
	
	UserResponse updateCurrentUser(UpdateUserRequest request);
	
	UserResponse patchCurrentUser(PatchUserRequest request);
	
	void deleteCurrentUser();
	
	List<TrackResponse> addFavoriteToCurrentUser(AddTrackRequest request);

	List<TrackResponse> getCurrentUserFavorites();

	void removeFavoriteFromCurrentUser(Long trackId);

	List<PlaylistResponse> getCurrentUserPlaylists();
	
	UserResponse getUserById(Long id);
	
	void deleteUser(Long id);
	
}
