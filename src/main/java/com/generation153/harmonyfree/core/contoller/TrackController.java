package com.generation153.harmonyfree.core.contoller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tracks")
public class TrackController {
	
	private final TrackService trackService;

    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }

}
