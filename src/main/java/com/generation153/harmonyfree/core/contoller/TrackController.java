package com.generation153.harmonyfree.core.contoller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.generation153.harmonyfree.core.dto.TrackSearchResponse;
import com.generation153.harmonyfree.core.dto.TrackSearchRequest;
import com.generation153.harmonyfree.core.service.TrackService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/tracks")
@Slf4j
public class TrackController {
	
	private final TrackService trackService;

    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }
    
    @GetMapping
    public List<TrackSearchResponse> getTracks(@Valid @ModelAttribute TrackSearchRequest request) {
    	log.info("request: " + request);
        return trackService.getTracks(request);
    }

}
