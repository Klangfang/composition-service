package com.klangfang.core.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.klangfang.core.Status;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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
    private Status status = Status.RELEASED;

    private Integer numberOfParticipants;

    public Composition() {}

    public Composition(String title, String creatorname, List<Track> tracks) {
        this.title = title;
        this.creatorname = creatorname;
        this.tracks = tracks;
    }

    public void release() { status = Status.RELEASED; }

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

    public void updateTracksAndRelease(List<Track> newTracks) {
        release();
        int trackPosition=0;
        for (Track newTrack : newTracks) {
            addNewSounds(trackPosition++, newTrack.getSounds());
        }
    }

    private void addNewSounds(int trackPosition, List<Sound> newSounds) {
        Track track = tracks.get(trackPosition);
        track.addSounds(newSounds);
    }

    public String getSnippet() {
        return tracks.get(0).getSounds().get(0).getFilename(); //TODO
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

    public Integer getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void refreshFilenames() {
        for (Track track : tracks) {
            track.refreshFilenames();
        }
    }
}
