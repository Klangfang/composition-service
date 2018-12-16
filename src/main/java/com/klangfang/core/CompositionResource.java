package com.klangfang.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlElement;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@ApiModel(description = "Model of composition small data")
public class CompositionResource extends ResourceSupport {

    @ApiModelProperty(notes = "Composition id")
    private Long compositionId;

    //TODO array byte in order to stream all sounds

    @ApiModelProperty(notes = "Creation date", position = 1)
    private LocalDateTime creationDate = LocalDateTime.now();

    @ApiModelProperty(notes = "Creator name", position = 2)
    private String creatorname;

    @ApiModelProperty(notes = "Duration in milliseconds", position = 3)
    private int durationInMs;

    public CompositionResource(Composition composition) {
        this.compositionId = composition.getId();
        this.creationDate = composition.getCreationDate();
        this.creatorname = composition.getCreatorname();
        this.durationInMs = composition.getDurationInMs();

        Link selfLink = linkTo(methodOn(CompositionController.class).getComposition(compositionId)).withSelfRel();
        add(selfLink);
    }

    public Long getCompositionId() {
        return compositionId;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public String getCreatorname() {
        return creatorname;
    }

    public int getDurationInMs() {
        return durationInMs;
    }


    /**
     * Returns all {@link Link}s contained in this resource.
     *
     * @return
     */
    @XmlElement(name = "link", namespace = Link.ATOM_NAMESPACE)
    @JsonProperty("links")
    @ApiModelProperty(notes = "Hateoas links", position = 4)
    public List<Link> getLinks() {
        return super.getLinks();
    }
}
