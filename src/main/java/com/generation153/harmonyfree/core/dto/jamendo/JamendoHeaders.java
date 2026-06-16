package com.generation153.harmonyfree.core.dto.jamendo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JamendoHeaders {
	
	private String status;

    private Integer code;

    @JsonProperty("error_message")
    private String errorMessage;

    @JsonProperty("results_count")
    private Integer resultsCount;

    @JsonProperty("results_fullcount")
    private Long resultsFullCount;

}
