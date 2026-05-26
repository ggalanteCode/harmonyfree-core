package com.generation153.harmonyfree.core.service.impl;

import java.util.List;

import com.generation153.harmonyfree.core.dto.AddTrackRequest;
import com.generation153.harmonyfree.core.dto.CreateUserRequest;
import com.generation153.harmonyfree.core.dto.PatchUserRequest;
import com.generation153.harmonyfree.core.dto.TrackResponse;
import com.generation153.harmonyfree.core.dto.UserResponse;
//import com.generation153.harmonyfree.core.service.PlaylistResponse;
import com.generation153.harmonyfree.core.service.UserService;

public class UserServiceImpl implements UserService {

	@Override
	public UserResponse getUserById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <UpdateUserRequest> UserResponse updateUser(Long id, UpdateUserRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteUser(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<TrackResponse> getUserFavorites(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeFavorite(Long userId, Long trackId) {
		// TODO Auto-generated method stub

	}

	// @Override
//	public List<PlaylistResponse> getUserPlaylists(Long userId) {
	// TODO Auto-generated method stub
	// return null;
	// }

	@Override
	public UserResponse createUser(CreateUserRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserResponse patchUser(Long id, PatchUserRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TrackResponse> addFavorite(Long userId, AddTrackRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
