package com.generation153.harmonyfree.core.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.generation153.harmonyfree.core.dto.TrackStatsResponse;
import com.generation153.harmonyfree.core.service.StatsService;

@RestController
@RequestMapping("/api/v1/stats")
public class StatsController {

	private final StatsService statsService;

	public StatsController(StatsService statsService) {
		this.statsService = statsService;
	}

	// TRACK PIU USATE
	// API: GET http://localhost:8080/api/v1/stats/tracks/most-played

	@GetMapping("/tracks/most-played")
	public List<TrackStatsResponse> getMostPlayedTracks(@RequestParam(defaultValue = "10") int limit) {

		return statsService.getMostPlayedTracks(limit);
	}

	// TRACK PIU AGGIUNTE AI PREFERITI
	// API: GET http://localhost:8080/api/v1/stats/tracks/most-favorited
	@GetMapping("/tracks/most-favorited")
	public List<TrackStatsResponse> getMostFavoritedTracks(@RequestParam(defaultValue = "10") int limit) {

		return statsService.getMostFavoritedTracks(limit);
	}
}
