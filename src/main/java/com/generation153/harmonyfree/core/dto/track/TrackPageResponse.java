package com.generation153.harmonyfree.core.dto.track;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@ToString
@Slf4j
public class TrackPageResponse {
	
	private List<TrackSearchResponse> content;
    private Integer offset;
    private Integer limit;
    private Long totalElements;
    private boolean hasNext;

}
