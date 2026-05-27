package com.generation153.harmonyfree.core.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JamendoSearchResponse {

    private List<JamendoTrackDto> results;
   
}
