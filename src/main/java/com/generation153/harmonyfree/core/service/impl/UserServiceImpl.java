package com.generation153.harmonyfree.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.generation153.harmonyfree.core.dto.AddTrackRequest;
import com.generation153.harmonyfree.core.dto.CreateUserRequest;
import com.generation153.harmonyfree.core.dto.PatchUserRequest;
import com.generation153.harmonyfree.core.dto.PlaylistResponse;
import com.generation153.harmonyfree.core.dto.TrackResponse;
import com.generation153.harmonyfree.core.dto.UserResponse;
import com.generation153.harmonyfree.core.entity.User;
import com.generation153.harmonyfree.core.exception.BadRequestException;
import com.generation153.harmonyfree.core.repository.UserRepository;
//import com.generation153.harmonyfree.core.service.PlaylistResponse;
import com.generation153.harmonyfree.core.service.UserService;

public class UserServiceImpl implements UserService {
	//INJECTION REPOSITORY
	public final UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	//GET USER BY ID (CREAZIONE UTENTE NEL DATABASE) DA COMPLETARE!!
	@Override
	public UserResponse getUserById(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new BadRequestException("User non trovato"));
		
		
		
		return mapToResponse(user);
	}

	@Override
	public <UpdateUserRequest> UserResponse updateUser(Long id, UpdateUserRequest request) {
		return null;
	}
	//CANCELLA USER
	@Override
	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}
	//TRACCE FAVORITE USER
	@Override
	public List<TrackResponse> getUserFavorites(Long userId) {
		return new ArrayList<>();
	}
	//RIMUOVE I PREFERITI
	@Override
	public void removeFavorite(Long userId, Long trackId) {

	}
	//PLAYLIST USER 
	@Override
	public List<PlaylistResponse> getUserPlaylists(Long userId) {
		return new ArrayList<>();
	}
	//CREAZIONE USER
	@Override
	public UserResponse createUser(CreateUserRequest request) {
		User user = new User();
		user.setUsername(request.getUsername());
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setEmail(request.getEmail());
		User saved = userRepository.save(user);
		return mapToResponse(saved);
	}
	
	@Override
	public UserResponse patchUser(Long id, PatchUserRequest request) {
		User user = userRepository.findById(id).orElseThrow(() -> new BadRequestException("User non trovato"));

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
		if (request.getDisplayName() != null) {
			user.setDisplayName(request.getDisplayName());
		}
		if (request.getProfileImageUrl() != null) {
			user.setProfileImageUrl(request.getProfileImageUrl());
		}
		return mapToResponse(userRepository.save(user));
	}
	
	@Override
	public List<TrackResponse> addFavorite(Long userId, AddTrackRequest request) {
		return null;
	}

	private UserResponse mapToResponse(User user) {
		UserResponse res = new UserResponse();
		res.setId(user.getId());
		res.setUsername(user.getUsername());
		res.setFirstName(user.getFirstName());
		res.setLastName(user.getLastName());
		res.setEmail(user.getEmail());
		return res;
	}
}
