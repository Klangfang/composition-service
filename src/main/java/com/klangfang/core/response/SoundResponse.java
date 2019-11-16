package com.klangfang.core.response;

import com.klangfang.core.entities.Sound;

public class SoundResponse {

    public Long id;

    public Integer trackNumber;

    public String url;

    public String title;

    public Integer startPosition;

    public Integer duration;

    public String creatorName;

    private SoundResponse(Long id,
                          Integer trackNumber,
                          String url,
                          String title,
                          Integer startPosition,
                          Integer duration,
                          String creatorName) {

        this.id = id;
        this.trackNumber = trackNumber;
        this.url = url;
        this.title = title;
        this.startPosition = startPosition;
        this.duration = duration;
        this.creatorName = creatorName;
    }

    public static SoundResponse build(Sound s) {

        return new SoundResponse(
                s.getId(),
                s.getTrackNumber(),
                s.getUrl(),
                s.getTitle(),
                s.getStartPosition(),
                s.getDuration(),
                s.getCreatorName()
        );

    }

}
