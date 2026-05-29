package com.generation153.harmonyfree.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.generation153.harmonyfree.core.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	@Query("""
		    SELECT u
		    FROM User u
		    LEFT JOIN FETCH u.favoriteTracks ft
		    LEFT JOIN FETCH ft.track
		    WHERE u.id = :id
		""")
		Optional<User> findByIdWithFavorites(Long id);
	
	boolean existsByFavoriteTracks_Track_Id(Long trackId);

	boolean existsByAuthUserId(Long authUserId);

}
