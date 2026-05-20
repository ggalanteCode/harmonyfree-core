package com.generation153.harmonyfree.core.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(
    name = "playlist_tracks",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"playlist_id", "track_id"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class PlaylistTrack {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id", nullable = false)
    private Playlist playlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id", nullable = false)
    private Track track;

    @Column(nullable = false)
    private Integer position;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

}
