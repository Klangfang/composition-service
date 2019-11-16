package com.klangfang.core.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.klangfang.core.common.type.AudioFileType.THREE_GP;

/**
 * Obwohl gleiche Sounds, hinzegefügt werden können. Wir betrachten jeden Sound besonders
 * TODO create unique constraints in db with (tracknumber,startposition,duration)
 */
@JsonIgnoreProperties
@Entity
public class Sound {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Min(0)
    @Column(nullable = false)
    private Integer trackNumber;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String title = LocalDateTime.now().toString() + THREE_GP.getName();

    @Min(0)
    @Column(nullable = false)
    private Integer startPosition = 0;

    @Min(0)
    @Column(nullable = false)
    private Integer duration = 0;

    @Column(nullable = false)
    private String creatorName;

    public Sound() {
    }

    public Sound(String creatorName, @Min(0) Integer startPosition,
                 @Min(0) int duration, Integer trackNumber, String url) {

        this.title = LocalDateTime.now().toString() + THREE_GP.getName();
        this.creatorName = creatorName;
        this.startPosition = startPosition;
        this.duration = duration;
        this.trackNumber = trackNumber;
        this.url = url;

    }

    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public Integer getStartPosition() {
        return startPosition;
    }

    public Integer getDuration() {
        return duration;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public String getTitle() {
        return title;
    }

    public Integer getTrackNumber() {
        return trackNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sound sound = (Sound) o;
        return trackNumber.equals(sound.trackNumber) &&
                startPosition.equals(sound.startPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trackNumber, startPosition);
    }

}
