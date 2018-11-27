package com.klangfang.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/compositions")
@ExposesResourceFor(Composition.class)
public class CompositionController {

    @Autowired
    private CompositionRepository compositionRepository;

    private final EntityLinks entityLinks;

    public CompositionController(EntityLinks entityLinks) {
        this.entityLinks = entityLinks;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    HttpEntity<Resources<Composition>> getAllCompositions() {

        Resources<Composition> resources = new Resources(compositionRepository.findAll());
        resources.add(this.entityLinks.linkToCollectionResource(Composition.class));
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }
}
