package com.generation153.harmonyfree.core.dto.stats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrackStatsResponse {

    private Long id;

    private String title;

    private String artist;

    private String album;

    private Long score;
}