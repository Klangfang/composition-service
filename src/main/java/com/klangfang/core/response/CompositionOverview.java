package com.klangfang.core.response;

public class CompositionOverview {

    public String title;
    public Integer numberOfMembers;
    public String snippetUrl;
    public String pickUrl;

    public CompositionOverview(String title, Integer numberOfMembers, String snippetUrl, String pickUrl) {
        this.title = title;
        this.numberOfMembers = numberOfMembers;
        this.snippetUrl = snippetUrl;
        this.pickUrl = pickUrl;
    }

}
