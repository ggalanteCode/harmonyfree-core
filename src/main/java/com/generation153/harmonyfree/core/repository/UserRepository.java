package com.generation153.harmonyfree.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.generation153.harmonyfree.core.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	boolean existsByFavoriteTracks_Track_Id(Long trackId);
	
	Optional<User> findByAuthUserId(Integer authUserId);
	
	@Query("""
		    SELECT u
		    FROM User u
		    LEFT JOIN FETCH u.favoriteTracks ft
		    LEFT JOIN FETCH ft.track
		    WHERE u.authUserId = :authUserId
		""")
	Optional<User> findByAuthUserIdWithFavorites(Integer authUserId);
	
	@Query("""
		    SELECT u
		    FROM User u
		    LEFT JOIN FETCH u.playlists p
		    WHERE u.authUserId = :authUserId
		""")
	Optional<User> findByAuthUserIdWithPlaylists(Integer authUserId);

	boolean existsByAuthUserId(Integer authUserId);
	
	void deleteByAuthUserId(Integer authUserId);

}
