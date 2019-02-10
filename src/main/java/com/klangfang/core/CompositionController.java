package com.klangfang.core;

import com.klangfang.core.entities.Composition;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/compositions")
@ExposesResourceFor(Composition.class)
public class CompositionController {

    private final CompositionRepository compositionRepository;

    private final EntityLinks entityLinks;

    @Autowired
    public CompositionController(CompositionRepository compositionRepository, EntityLinks entityLinks) {
        this.compositionRepository = compositionRepository;
        this.entityLinks = entityLinks;
    }

    private final String APPLICATION_HAL_JSON = "application/hal+json";

    @ApiOperation("Gets a list of composition having the given status")
    @GetMapping(params = {"status"}, produces = APPLICATION_HAL_JSON)
    @Transactional
    HttpEntity<Resources<Composition>> getCompositionsByStatus(@RequestParam String status) {

        List<Composition> compositions = compositionRepository.findByStatus(
                Status.valueOf(status.toUpperCase()));
        Resources<Composition> resources = new Resources<>(compositions);
        resources.add(this.entityLinks.linkToCollectionResource(Composition.class));

        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @ApiOperation("Create a new composition")
    @PostMapping(produces = APPLICATION_HAL_JSON)
    HttpEntity<Resource<Composition>> createComposition(@RequestBody Composition composition) {
        compositionRepository.save(composition);
        Resource<Composition> resource = new Resource(composition);
        resource.add(this.entityLinks.linkToSingleResource(Composition.class, composition.getId()));
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @ApiOperation("Gets all informations about a composition")
    @GetMapping(path = "/{compositionId}", produces = APPLICATION_HAL_JSON)
    HttpEntity<Resource<Composition>> getComposition(@PathVariable Long compositionId) {

        Composition composition = compositionRepository.getOne(compositionId);
        Resource<Composition> resource = new Resource(composition);
        resource.add(this.entityLinks.linkToSingleResource(Composition.class, compositionId));
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @ApiOperation("Updates the tracks of a composition")
    @PutMapping(path = "/{compositionId}", produces = APPLICATION_HAL_JSON)
    HttpEntity<Resource<Composition>> updateCompositionTracks(@PathVariable("compositionId") Long compositionId/*,
                                                                          @RequestPart("tracks") List<Track> tracks,*/) {

        Composition result = compositionRepository.getOne(compositionId);
        Resource<Composition> resource = null;
        if (result != null) {
            //result.updateTracks(tracks); // TODO use a merge instead
            compositionRepository.save(result);//TODO replace with transaction save
            resource = new Resource(result);
        }
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }
}
