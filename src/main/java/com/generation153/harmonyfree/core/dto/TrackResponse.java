package com.generation153.harmonyfree.core.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TrackResponse {
	
	private Long id;          // DB ID
	private Long jamendoTrackId;

	private String title;
	private String artist;
	private String album;
	private List<String> genres;
	private Integer duration;

	private String coverImageUrl;	//per poter visualizzare la copertina su frontend
	private String audioUrl;	//per poter accedere al brano da ascoltare tramite frontend
	private LocalDateTime createdAt;

}
