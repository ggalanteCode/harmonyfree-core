package com.generation153.harmonyfree.core.service;

import java.util.List;

import com.generation153.harmonyfree.core.dto.AddTrackRequest;
import com.generation153.harmonyfree.core.dto.CreatePlaylistRequest;
import com.generation153.harmonyfree.core.dto.PlaylistResponse;
import com.generation153.harmonyfree.core.dto.TrackResponse;
import com.generation153.harmonyfree.core.dto.UpdatePlaylistRequest;

public interface PlaylistService {

    PlaylistResponse createPlaylist(CreatePlaylistRequest request);

    PlaylistResponse getPlaylistById(Long id);

	PlaylistResponse updatePlaylist(Long id, UpdatePlaylistRequest request);

	PlaylistResponse patchPlaylist(Long id, UpdatePlaylistRequest request);

	void deletePlaylist(Long id);
	
	List<TrackResponse> getPlaylistTracks(Long playlistId);

	PlaylistResponse addTrackToPlaylist(Long playlistId, AddTrackRequest request);
	
	void removeTrackFromPlaylist(Long playlistId, Long trackId);
	
}