package com.generation153.harmonyfree.core.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation153.harmonyfree.core.dto.track.TrackPageResponse;
import com.generation153.harmonyfree.core.dto.track.TrackResponse;
import com.generation153.harmonyfree.core.dto.track.TrackSearchRequest;
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
    
    //LE CHIAMATE DEL TrackController SONO TUTTE PUBBLICHE
    
    @GetMapping
    public TrackPageResponse getTracks(@Valid @ModelAttribute TrackSearchRequest request) {
    	log.info("request: " + request);
        return trackService.getTracks(request);
    }
    
    @GetMapping("/{id}")
    public TrackResponse getTrackById(@PathVariable("id") String jamendoTrackId) {
        return trackService.getTrackById(Long.parseLong(jamendoTrackId));
    }

}
