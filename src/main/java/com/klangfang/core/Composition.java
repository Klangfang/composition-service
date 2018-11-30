package com.klangfang.core;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class Composition {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Track> tracks;

    private LocalDateTime creationDate = LocalDateTime.now();

    @Column(nullable = false)
    private String creatorname;

    private CompositionStatus status;

    public Composition() {}

    public Composition(String creatorname, List<Track> tracks) {
        this.creatorname = creatorname;
        this.tracks = tracks;
    }

    public void addTrack(Track track) {
        tracks.add(track);
    }

    public int getDurationInMs() {
        int duration = tracks.stream()
                .filter(Objects::nonNull)
                .mapToInt(Track::getDurationInMs)
                .sum();

        return duration;
    }

    public int getTrackPosition(Track track) {
        return tracks.indexOf(track);
    }

    public Long getId() {
        return id;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public String getCreatorname() {
        return creatorname;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }
}
