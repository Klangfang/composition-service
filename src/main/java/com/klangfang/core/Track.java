package com.klangfang.core;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Sound> sounds;

    public Track() {}

    public Track(List<Sound> sounds) {
        this.sounds = sounds;
    }

    public int getDurationInMs() {
        int duration = sounds.stream()
                .filter(Objects::nonNull)
                .mapToInt(Sound::getDurationInMs)
                .sum();

        return duration;
    }

    //TODO Die Reihenfolge durch Startposition setzten und so auch in die Liste speichen
    public void addSounds(List<Sound> newSounds) {
        sounds.addAll(newSounds);
    }

    public Long getId() {
        return id;
    }

    public List<Sound> getSounds() {
        return sounds;
    }
}
