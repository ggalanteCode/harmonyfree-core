package com.generation153.harmonyfree.core.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name = "tracks")
@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class Track {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jamendo_track_id", nullable = false, unique = true)
    private Long jamendoTrackId;

    @Column(name = "jamendo_artist_id")
    private Long jamendoArtistId;

    @Column(name = "jamendo_album_id")
    private Long jamendoAlbumId;

    @Column(nullable = false)
    private String title;

    @Column(name = "artist_name")
    private String artistName;

    @Column(name = "album_name")
    private String albumName;

    @OneToMany(mappedBy = "track", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrackGenre> trackGenres = new ArrayList<>();

    private Integer duration;

    @Column(name = "audio_url", columnDefinition = "TEXT")
    private String audioUrl;

    @Column(name = "cover_url", columnDefinition = "TEXT")
    private String coverUrl;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "track")
    private List<PlaylistTrack> playlistTracks = new ArrayList<>();

    @OneToMany(mappedBy = "track")
    private List<UserFavoriteTrack> favoriteTracks = new ArrayList<>();
    
    /*
     * METODI HELPER
     */
    
    public void addGenre(Genre genre) {
    	
        if (this.trackGenres == null) {
            this.trackGenres = new ArrayList<>();
        }

        TrackGenre trackGenre = new TrackGenre();
        trackGenre.setTrack(this);
        trackGenre.setGenre(genre);

        this.trackGenres.add(trackGenre);
    }

}
