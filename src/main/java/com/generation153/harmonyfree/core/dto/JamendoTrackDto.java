package com.generation153.harmonyfree.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JamendoTrackDto {

    private Long id;
    
    private String name;
    
    @JsonProperty("artist_name")
    private String artistName;

    @JsonProperty("album_name")
    private String albumName;

    private MusicInfo musicinfo;

    private Long artistId;
    private Long albumId;
    
	
    
}
