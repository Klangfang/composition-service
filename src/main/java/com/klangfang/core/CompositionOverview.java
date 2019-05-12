package com.klangfang.core;

public class CompositionOverview {

    private String title;
    private Integer numberOfMembers;
    private String snippet;

    public CompositionOverview(String title, Integer numberOfMembers, String snippet) {
        this.title = title;
        this.numberOfMembers = numberOfMembers;
        this.snippet = snippet;
    }

    public String getTitle() {
        return title;
    }

    public Integer getNumberOfMembers() {
        return numberOfMembers;
    }

    public String getSnippet() {
        return snippet;
    }
}
