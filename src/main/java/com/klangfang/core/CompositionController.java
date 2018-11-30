package com.klangfang.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(path = "/compose", produces = MediaType.APPLICATION_JSON_VALUE)
    HttpEntity<Resources<Composition>> getComposeCompositions() {

        Resources<Composition> resources = new Resources(compositionRepository.findAll());
        resources.add(this.entityLinks.linkToCollectionResource(Composition.class));
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }


    @GetMapping(path = "/discover", produces = MediaType.APPLICATION_JSON_VALUE)
    HttpEntity<Resources<Composition>> getDiscoveredCompositions() {

        Resources<Composition> resources = new Resources(compositionRepository.findAll());
        resources.add(this.entityLinks.linkToCollectionResource(Composition.class));
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    // Create new composition
    @PostMapping
    HttpEntity<Resources<String>> createComposition(@RequestBody Composition composition) {

        compositionRepository.save(composition);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    HttpEntity<Resource<Composition>> getComposition(@PathVariable Long id) {

        return null;
    }

    @PutMapping
    HttpEntity<Resources<String>> updateComposition(@RequestBody Composition composition) {

        return null;
    }



}
