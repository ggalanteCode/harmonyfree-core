package com.generation153.harmonyfree.core.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JamendoSearchResponse {

    private List<JamendoTrackDto> results;

	@Override
	public String toString() {
		return "JamendoSearchResponse [results=" + results + "]";
	}
   
}
