package com.generation153.harmonyfree.core.dto;

public class TrackResponse {
	
	private Long id;
    private String title;
    private String artist;
    private String album;

    public TrackResponse(Long id, String title, String artist, String album) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
    }

}
