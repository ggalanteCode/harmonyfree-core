package com.generation153.harmonyfree.core.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackSearchRequest {
	
	@Size(max = 100)
    private String query;

    @Size(max = 100)
    private String title;

    @Size(max = 100)
    private String artist;

    @Size(max = 100)
    private String album;

    @Size(max = 50)
    private String genre;

    @Min(0)
    private int offset = 0;

    @Min(1)
    @Max(50)
    private int limit = 20;
    
	@Override
	public String toString() {
		return "TrackSearchRequest [query=" + query + ", title=" + title + ", artist=" + artist + ", album=" + album
				+ ", genre=" + genre + ", offset=" + offset + ", limit=" + limit + "]";
	}
    
    

}
