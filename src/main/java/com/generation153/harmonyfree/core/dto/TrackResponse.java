package com.generation153.harmonyfree.core.dto;

import java.time.LocalDateTime;
import java.util.List;

public class TrackResponse {
	
	private Long id;          // DB ID
	private Long jamendoId;

	private String title;
	private String artist;
	private String album;

	private List<String> genres;

	private String audioUrl;
	private String coverImage;
	private LocalDateTime createdAt;

}
