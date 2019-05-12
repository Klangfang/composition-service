package com.klangfang.core;

public class CompositionOverview {

    private String title;
    private Integer numberOfMembers;
    private String snippetPath;

    public CompositionOverview(String title, Integer numberOfMembers, String snippetPath) {
        this.title = title;
        this.numberOfMembers = numberOfMembers;
        this.snippetPath = snippetPath;
    }

    public String getTitle() {
        return title;
    }

    public Integer getNumberOfMembers() {
        return numberOfMembers;
    }

    public String getSnippetPath() {
        return snippetPath;
    }
}
