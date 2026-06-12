package com.generation153.harmonyfree.core.dto.jamendo;

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
    
    private Integer duration;
    
    @JsonProperty("audio")
    private String audioUrl;	//URL dell'audio da ascoltareù
    
    @JsonProperty("image")
    private String coverUrl;	//URL della copertina

    private MusicInfo musicinfo;	//contiene i metadati dell'album, tra cui i genre

    @JsonProperty("artist_id")
    private Long artistId;
    
    @JsonProperty("album_id")
    private Long albumId;
    
    @JsonProperty("audiodownload")
    private String downloadUrl;	//URL per l'eventuale download del brano

    @JsonProperty("audiodownload_allowed")
    private Boolean downloadable;	//ci dice se è possibile scaricare o meno il brano.
    
}
