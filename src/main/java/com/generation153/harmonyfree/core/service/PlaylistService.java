package com.generation153.harmonyfree.core.service;

import com.generation153.harmonyfree.core.dto.CreatePlaylistRequest;
import com.generation153.harmonyfree.core.dto.PlaylistResponse;

public interface PlaylistService {

    PlaylistResponse createPlaylist(CreatePlaylistRequest request);

    PlaylistResponse getPlaylistById(Long id);
}