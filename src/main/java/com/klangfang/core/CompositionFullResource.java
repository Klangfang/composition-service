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

@ApiModel(description = "Model of composition all data")
public class CompositionFullResource extends ResourceSupport {

    @ApiModelProperty(notes = "Composition id")
    private Long compositionId;

    @ApiModelProperty(notes = "List of tracks", position = 1)
    private List<Track> tracks;

    @ApiModelProperty(notes = "Creation date", position = 2)
    private LocalDateTime creationDate = LocalDateTime.now();

    @ApiModelProperty(notes = "Creator name", position = 3)
    private String creatorname;

    @ApiModelProperty(notes = "Current status", position = 4)
    private CompositionStatus status;

    public CompositionFullResource(Composition composition) {
        this.compositionId = composition.getId();
        this.tracks = composition.getTracks();
        this.creationDate = composition.getCreationDate();
        this.creatorname = composition.getCreatorname();
        this.status = composition.getStatus();

        Link selfLink = linkTo(methodOn(CompositionController.class).getComposition(compositionId)).withSelfRel();
        add(selfLink);
    }

    public Long getCompositionId() {
        return compositionId;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public String getCreatorname() {
        return creatorname;
    }

    public CompositionStatus getStatus() {
        return status;
    }

    /**
     * Returns all {@link Link}s contained in this resource.
     *
     * @return
     */
    @XmlElement(name = "link", namespace = Link.ATOM_NAMESPACE)
    @JsonProperty("links")
    @ApiModelProperty(notes = "Hateoas links", position = 5)
    public List<Link> getLinks() {
        return super.getLinks();
    }
}
