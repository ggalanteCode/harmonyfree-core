package com.generation153.harmonyfree.core.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TrackResponse {
	
	private Long id;          // DB ID
	private Long jamendoTrackId;

	private String title;
	private String artist;
	private String album;
	private List<String> genres;

	private String coverImageUrl;	//per poter visualizzare la copertina su frontend
	private String audioUrl;	//per poter accedere al brano da ascoltare tramite frontend
	private LocalDateTime createdAt;

}
