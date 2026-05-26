package com.generation153.harmonyfree.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@Slf4j
public class TrackSearchResponse {
	
	private Long id;
    private String title;
    private String artist;
    private String album;

}
