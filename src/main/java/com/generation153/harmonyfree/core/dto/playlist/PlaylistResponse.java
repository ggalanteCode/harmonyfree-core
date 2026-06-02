package com.generation153.harmonyfree.core.dto.playlist;

import java.time.LocalDateTime;
import java.util.List;

import com.generation153.harmonyfree.core.dto.track.TrackResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistResponse {

    private Long id;

    private String title;

    private String description;
    
    private Boolean isPublic;

    private LocalDateTime createdAt;

    private List<TrackResponse> tracks;

}