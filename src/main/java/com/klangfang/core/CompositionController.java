package com.klangfang.core;

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

import java.util.Arrays;

@RestController
@RequestMapping("/compositions")
@ExposesResourceFor(Composition.class)
public class CompositionController {

    private final EntityLinks entityLinks;

    public CompositionController(EntityLinks entityLinks) {
        this.entityLinks = entityLinks;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    HttpEntity<Resources<Composition>> getAllCompositions() {

        Resources<Composition> resources = new Resources(Arrays.asList(new Composition(), new Composition(), new Composition()));
        resources.add(this.entityLinks.linkToCollectionResource(Composition.class));
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }
}
