package com.klangfang.core;

import com.klangfang.core.entities.type.Status;
import com.klangfang.core.entities.Composition;
import com.klangfang.core.response.CompositionOverview;
import com.klangfang.core.response.CompositionResponse;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class ResponseTransformer {


    public CompositionOverview overview(Composition composition) {

        String pickUrl = ControllerLinkBuilder.linkTo(methodOn(CompositionController.class)
                .pick(composition.getId())).withRel("pick").getHref();

        String snippetUrl = linkTo(methodOn(CompositionController.class)
                .downloadFile(composition.getId(), composition.getSnippet(), null)).withRel("snippet").getHref();

        CompositionOverview overview =
                new CompositionOverview(composition.getTitle(), composition.getNumberOfMembers(), snippetUrl, pickUrl);

        return overview;
    }

    public CompositionResponse fullResponse(Composition composition) {

        CompositionResponse response;

        if (composition.getStatus() == Status.PICKED) {
            response = new CompositionResponse(composition,
                    linkTo(methodOn(CompositionController.class).release(composition.getId(), null, null))
                            .withRel("release").getHref());
        } else {
            response = new CompositionResponse(composition);
        }

        return response;
    }

    public String getSoundRelativePath(Long compositionId, String filePath) {

        String soundRelativePath = linkTo(methodOn(CompositionController.class)
                .downloadFile(compositionId, filePath, null)).toString();
        return soundRelativePath;
    }
}
