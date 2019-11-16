package com.klangfang.core.entities;

import com.klangfang.core.entities.type.Status;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Track vs. No Track
 * Je mehr compositions und je mehr sounds desto mehr tracknr. (immer Track-Beziehungen weniger)
 * Je mehr compositions desto mehr tracks Beziehungen (Unanbhängig von der Soundanzahl, immer fest 4 tracks)
 */
@Entity
public class Composition {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //TODO @NaturalId
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String creatorName;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Sound> sounds;

    private LocalDateTime creationDate = LocalDateTime.now();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.AVAILABLE;

    private Integer numberOfMembers = 1;

    public Composition() {}

    public Composition(String title, String creatorName, List<Sound> sounds) {
        this.title = title;
        this.creatorName = creatorName;
        this.sounds = sounds;
        sortSounds();
    }


    //TODO sort by tracknr and startposition (Frontend muss nur noch die sounds für die jeweilige tracknummer raussuchen)
    //Keine notwendige Sortierung im Frontend mehr?!
    private void sortSounds() {
    }

    public void release() { status = Status.AVAILABLE; }

    public void pick() {
        status = Status.PICKED;
        numberOfMembers++;
        sounds = sounds.stream().map(sound -> getSoundWithRelativePath(sound)).collect(Collectors.toList());
    }

    private Sound getSoundWithRelativePath(Sound sound) {
        //sound.setRelativePath(id);
        Sound soundWithRelativePath = sound;
        return soundWithRelativePath;
    }

    public void block() {
        status = Status.BLOCKED;
    }

    public void close() {
        status = Status.COMPLETED;
    }


    public void addSounds(List<Sound> newSounds) {
        sounds.addAll(newSounds);
        sortSounds();
        release();
    }

    public void addSounds() {
        sortSounds();
        release();
    }

    public String getSnippet() {
        return sounds.get(0).getUrl();
    }

    public int getDuration() {
        int duration = sounds.stream()
                .filter(Objects::nonNull)
                .mapToInt(Sound::getDuration)
                .sum();

        return duration;
    }

    public Long getId() {
        return id;
    }

    public List<Sound> getSounds() {
        return sounds;
    }

    public String getCreatorName() {
        return creatorName;
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

    public Integer getNumberOfMembers() {
        return numberOfMembers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Composition that = (Composition) o;
        return title.equals(that.title) &&
                sounds.equals(that.sounds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, sounds);
    }

    public String getSoundTile(int soundPosition) {

        return sounds.get(soundPosition).getTitle();

    }
}
