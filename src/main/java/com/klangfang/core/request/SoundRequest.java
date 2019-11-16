package com.klangfang.core.request;

import com.klangfang.core.entities.Sound;

public class SoundRequest {

    public Integer trackNumber;

    public Integer startPosition;

    public Integer duration;

    public String creatorName;

    public byte[] soundBytes;


    public Sound toEntity(String soundURL) {

        return new Sound(creatorName, startPosition, duration, trackNumber, soundURL);

    }

}
