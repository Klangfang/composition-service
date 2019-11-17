package com.klangfang.core.entities;

import com.klangfang.core.entities.type.Status;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Track vs. No Track
 * Je mehr compositions und je mehr sounds desto mehr tracknr. (immer Track-Beziehungen weniger)
 * Je mehr compositions desto mehr tracks Beziehungen (Unanbhängig von der Soundanzahl, immer fest 4 tracks)
 */
@Entity
public class Composition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String creatorName;

    //mappedBy = "composition"
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Sound> sounds;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.AVAILABLE;

    private Integer numberOfMembers = 1;

    @Column(nullable = false)
    private Integer maximumNumberOfMembers;

    @Column(nullable = false)
    private ZonedDateTime creationTime = ZonedDateTime.now();

    public Composition() {}

    public Composition(String title, String creatorName, List<Sound> sounds, Integer maximumNumberOfMembers) {
        this.title = title;
        this.creatorName = creatorName;
        this.sounds = sounds;
        this.maximumNumberOfMembers = maximumNumberOfMembers;
        sortSounds();
    }

    public boolean hasReachedLimitOfMembers() {
        return numberOfMembers == maximumNumberOfMembers;
    }

    //TODO sort by tracknr and startposition (Frontend muss nur noch die sounds für die jeweilige tracknummer raussuchen)
    //Keine notwendige Sortierung im Frontend mehr?!
    private void sortSounds() {
    }

    public boolean isAvailable() {
        return status == Status.AVAILABLE;
    }

    public boolean isPicked() {
        return status == Status.PICKED;
    }

    private void release() {
        status = Status.AVAILABLE;
    }

    public void pick() {
        status = Status.PICKED;
    }

    private void complete() {
        status = Status.COMPLETED;
    }

    public void join(List<Sound> sounds) {

        numberOfMembers++;
        addSounds(sounds);
        if (hasReachedLimitOfMembers()) {
            complete();
        } else {
            release();
        }

    }

    public void cancel() {

        release();

    }

    private void addSounds(List<Sound> newSounds) {
        sounds.addAll(newSounds);
        sortSounds();
        release();
    }

    public String getSnippet() {
        if (sounds.isEmpty()) {
            throw new RuntimeException("Can not find snippet due to sound list is empty");
        }
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

    public ZonedDateTime getCreationTime() {
        return creationTime;
    }

    public String getStatus() {
        return status.name();
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
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
