package com.generation153.harmonyfree.core.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlaylistResponse {

    private Long id;

    private String title;

    private String description;

    private LocalDateTime createdAt;

    
    private List<TrackResponse> tracks;

}