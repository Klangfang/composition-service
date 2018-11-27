package com.klangfang.core;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
public class Sound {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @NaturalId
    private String filename;

    @Min(0)
    @Column(nullable = false)
    private int startPositionInMs;

    @Min(0)
    @Column(nullable = false)
    private int durationInMs;

    @Column(nullable = false)
    private String creatorname;

    public Sound() {
    }

    public Sound(@Min(0) int startPositionInMs, @Min(0) int durationInMs, String filename, String creatorname) {
        this.startPositionInMs = startPositionInMs;
        this.durationInMs = durationInMs;
        this.filename = filename;
        this.creatorname = creatorname;
    }

    public Long getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public int getStartPositionInMs() {
        return startPositionInMs;
    }

    public int getDurationInMs() {
        return durationInMs;
    }

    public String getCreatorname() {
        return creatorname;
    }
}
