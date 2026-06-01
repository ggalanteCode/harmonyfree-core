package com.generation153.harmonyfree.core.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.generation153.harmonyfree.core.dto.playlist.CreatePlaylistRequest;
import com.generation153.harmonyfree.core.dto.playlist.PlaylistResponse;
import com.generation153.harmonyfree.core.dto.playlist.UpdatePlaylistRequest;
import com.generation153.harmonyfree.core.dto.track.AddTrackRequest;
import com.generation153.harmonyfree.core.dto.track.TrackResponse;
import com.generation153.harmonyfree.core.dto.track.TrackSearchResponse;
import com.generation153.harmonyfree.core.service.PlaylistService;


@RestController
@RequestMapping("/api/v1/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    // API: POST http://localhost:8080/api/v1/playlists
    @PostMapping
    public PlaylistResponse createPlaylist(@RequestBody CreatePlaylistRequest request) {
        return playlistService.createPlaylist(request);
    }

    // API: GET http://localhost:8080/api/v1/playlists/{id}
    @GetMapping("/{id}")
    public PlaylistResponse getPlaylistById(@PathVariable Long id) {
        return playlistService.getPlaylistById(id);
    }
    
 // API: PUT http://localhost:8080/api/v1/playlists/{id}
    @PutMapping("/{id}")
    public PlaylistResponse updatePlaylist(
            @PathVariable Long id,
            @RequestBody UpdatePlaylistRequest request) {

        return playlistService.updatePlaylist(id, request);
    }
 // API: PATCH http://localhost:8080/api/v1/playlists/{id}
    @PatchMapping("/{id}")
    public PlaylistResponse patchPlaylist(
            @PathVariable Long id,
            @RequestBody UpdatePlaylistRequest request) {

        return playlistService.patchPlaylist(id, request);
    }
 // API: DELETE http://localhost:8080/api/v1/playlists/{id}
    @DeleteMapping("/{id}")
    public void deletePlaylist(@PathVariable Long id) {
        playlistService.deletePlaylist(id);
    }
 // API: GET http://localhost:8080/api/v1/playlists/{id}/tracks
    @GetMapping("/{id}/tracks")
    public List<TrackResponse> getPlaylistTracks(@PathVariable Long id) {

        return playlistService.getPlaylistTracks(id);
    }
 // API: POST http://localhost:8080/api/v1/playlists/{id}/tracks
    @PostMapping("/{id}/tracks")
    public PlaylistResponse addTrackToPlaylist(
            @PathVariable Long id,
            @RequestBody AddTrackRequest request) {

        return playlistService.addTrackToPlaylist(id, request);
    }
 // API: DELETE http://localhost:8080/api/v1/playlists/{playlistId}/tracks/{trackId}
    @DeleteMapping("/{playlistId}/tracks/{trackId}")
    public void removeTrackFromPlaylist(
            @PathVariable Long playlistId,
            @PathVariable Long trackId) {

        playlistService.removeTrackFromPlaylist(playlistId, trackId);
    }
 
}
