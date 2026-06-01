package com.generation153.harmonyfree.core.service;

import java.util.List;

import com.generation153.harmonyfree.core.dto.playlist.CreatePlaylistRequest;
import com.generation153.harmonyfree.core.dto.playlist.PlaylistResponse;
import com.generation153.harmonyfree.core.dto.playlist.UpdatePlaylistRequest;
import com.generation153.harmonyfree.core.dto.track.AddTrackRequest;
import com.generation153.harmonyfree.core.dto.track.TrackResponse;

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