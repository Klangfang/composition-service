package com.klangfang.core;

public class CompositionOverviewDto {

    private String title;
    private Integer numberOfParticipation;
    private String soundFilePath;

    public CompositionOverviewDto(String title, Integer numberOfParticipation, String soundFilePath) {
        this.title = title;
        this.numberOfParticipation = numberOfParticipation;
        this.soundFilePath = soundFilePath;
    }

    public String getTitle() {
        return title;
    }

    public Integer getNumberOfParticipation() {
        return numberOfParticipation;
    }

    public String getSoundFilePath() {
        return soundFilePath;
    }
}
