package com.klangfang.core.entities;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;

@Entity
//@Table(name = "sound    ", schema = "compositions")
public class Sound implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NaturalId
    @Column(nullable = false)
    private String filename;

   // @Column(nullable = false)
    private String title;

    @Min(0)
    //@Column(nullable = false)
    private int startPositionInMs;

    @Min(0)
    @Column(nullable = false)
    private int durationInMs;

    @Column(nullable = false)
    private String creatorname;

    public Sound() {
    }

    public Sound(String filename, String title, String creatorname, @Min(0) int startPositionInMs,
                 @Min(0) int durationInMs) {
        this.filename = filename;
        this.title = title;
        this.creatorname = creatorname;
        this.startPositionInMs = startPositionInMs;
        this.durationInMs = durationInMs;
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

    public String getTitle() {
        return title;
    }

    public void refreshFilename() {

    }
}
