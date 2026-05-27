package com.generation153.harmonyfree.core.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JamendoTrackDto {

    private Long id;
    private String name;
    private String artist_name;
    private String album_name;
    private String tags;

    private Long artistId;
    private Long albumId;
    
	@Override
	public String toString() {
		return "JamendoTrackDto [id=" + id + ", name=" + name + ", artist_name=" + artist_name + ", album_name="
				+ album_name + ", tags=" + tags + ", artistId=" + artistId + ", albumId=" + albumId + "]";
	}
    
	
    
    
}
