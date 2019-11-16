package com.klangfang.core.response;

import com.klangfang.core.entities.Composition;

public class CompositionOverview {

    public Long id;
    public String title;
    public Integer numberOfMembers;
    public String snippetUrl;

    private CompositionOverview(Long id,
                                String title,
                                Integer numberOfMembers,
                                String snippetUrl) {
        this.id = id;
        this.title = title;
        this.numberOfMembers = numberOfMembers;
        this.snippetUrl = snippetUrl;

    }

    public static CompositionOverview build(Composition c) {

        return new CompositionOverview(
                c.getId(),
                c.getTitle(),
                c.getNumberOfMembers(),
                c.getSnippet()
        );

    }

}
