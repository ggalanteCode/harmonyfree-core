package com.generation153.harmonyfree.core.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Slf4j
public class TrackSearchResponse {
	
	private Long id; //jamendo id
    private String title;
    private String artist;
    private String album;
    private List<String> genres;
    private Integer duration;
    
    //vogliamo che questi dati siano disponibili anche durante la ricerca semplice con filtri
    private String coverImageUrl;	//per poter visualizzare la copertina su frontend
    private String audioUrl;	//per poter accedere al brano da ascoltare tramite frontend

}
