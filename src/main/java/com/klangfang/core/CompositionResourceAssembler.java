package com.klangfang.core;

import com.klangfang.core.entities.Composition;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class CompositionResourceAssembler implements ResourceAssembler<Composition, Resource<CompositionOverviewDto>> {


    @Override
    public Resource<CompositionOverviewDto> toResource(Composition composition) {

        Resource<CompositionOverviewDto> compositionResource = new Resource<>(
                new CompositionOverviewDto(composition.getTitle(), composition.getNumberOfParticipants(),
                        FilePathUtils.generateSoundFilePath("uri", composition.getId(), composition.getSnippet())));

        if (composition.getStatus() == Status.RELEASED) {
            compositionResource.add(
                    linkTo(methodOn(CompositionController.class)
                            .pick(composition.getId())).withRel("pick"));
        }

        return compositionResource;
    }

    public Resource<Composition> toFullResource(Composition composition) {

        Resource<Composition> compositionFullResource = new Resource<>(composition);

        if (composition.getStatus() == Status.PICKED) {
            compositionFullResource.add(
                    linkTo(methodOn(CompositionController.class)
                            .release(composition.getId(), null, null)).withRel("release"));
        }

        return compositionFullResource;
    }
}
