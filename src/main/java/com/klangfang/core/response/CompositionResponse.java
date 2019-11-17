package com.klangfang.core.response;

import com.klangfang.core.entities.Composition;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CompositionResponse {

    public Long id;

    public String title;

    public String creatorName;

    public ZonedDateTime creationTime;

    public String status;

    public Integer numberOfMembers;

    public Integer duration;

    public String snippet;

    public List<SoundResponse> sounds;

    private CompositionResponse(Long id,
                                String title,
                                String creatorName,
                                ZonedDateTime creationTime,
                                String status,
                                Integer numberOfMembers,
                                Integer duration,
                                String snippet,
                                List<SoundResponse> sounds) {

        this.id = id;
        this.title = title;
        this.creatorName = creatorName;
        this.creationTime = creationTime;
        this.status = status;
        this.numberOfMembers = numberOfMembers;
        this.duration = duration;
        this.snippet = snippet;
        this.sounds = sounds;

    }

    public static CompositionResponse build(Composition c) {

        return new CompositionResponse(
                c.getId(),
                c.getTitle(),
                c.getCreatorName(),
                c.getCreationTime(),
                c.getStatus(),
                c.getNumberOfMembers(),
                c.getDuration(),
                c.getSnippet(),
                c.getSounds()
                        .stream().map(SoundResponse::build)
                        .collect(Collectors.toList())
        );

    }
}
