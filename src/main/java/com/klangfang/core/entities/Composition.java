package com.klangfang.core.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.klangfang.core.Status;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
//@Table(name = "composition", schema = "compositions")
public class Composition implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String creatorname;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Track> tracks;

    private LocalDateTime creationDate = LocalDateTime.now();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.CREATED;

    public Composition() {}

    public Composition(String title, String creatorname, List<Track> tracks) {
        this.title = title;
        this.creatorname = creatorname;
        this.tracks = tracks;
    }

    public void pick() {
        status = Status.PICKED;
    }

    public void block() {
        status = Status.BLOCKED;
    }

    public void close() {
        status = Status.CLOSED;
    }

    @JsonIgnore
    public List<String> getFilenames() {
        return tracks.stream().
                flatMap(t -> t.getFilenames().stream())
                .collect(Collectors.toList());
    }

    @JsonIgnore
    public List<byte[]> getFiles() {
        return tracks.stream().
                flatMap(t -> t.getFiles().stream())
                .collect(Collectors.toList());
    }

    //TODO replace with a merge function
    public void updateTracks(List<Track> newTracks) {
        int trackPosition=0;
        for (Track newTrack : newTracks) {
            List<Sound> newSounds = newTrack.getSounds();
            addNewSounds(trackPosition++, newSounds);
        }
    }

    private void addNewSounds(int trackPosition, List<Sound> newSounds) {
        Track track = tracks.get(trackPosition);
        track.addSounds(newSounds);
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

    public Status getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public void refreshFilenames() {
        for (Track track : tracks) {
            track.refreshFilenames();
        }
    }
}
