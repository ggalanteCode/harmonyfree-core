package com.generation153.harmonyfree.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation153.harmonyfree.core.entity.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
	
	Optional<Genre> findByName(String name);

}
