package com.klangfang.core;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/compositions")
@ExposesResourceFor(Composition.class)
public class CompositionController {

    private final CompositionRepository compositionRepository;

    @Autowired
    public CompositionController(CompositionRepository compositionRepository) {
        this.compositionRepository = compositionRepository;
    }

    private final String APPLICATION_HAL_JSON = "application/hal+json";

    //Z.B compositions?status=created
    @ApiOperation("Gets a list of composition having the given status")
    @GetMapping(params = {"status"}, produces = APPLICATION_HAL_JSON)
    @Transactional
    HttpEntity<Resources<CompositionResource>> getCompositionsByStatus(@RequestParam String status) {

        List<Composition> compositions = compositionRepository.findByStatus(CompositionStatus.valueOf(status.toUpperCase()));
        Resources<CompositionResource> resources = new Resources(compositions.stream().map(c -> new CompositionResource(c)).collect(Collectors.toList()));
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @ApiOperation("Create a new composition")
    @PostMapping(produces = APPLICATION_HAL_JSON)
    HttpEntity<Resource<CompositionFullResource>> createComposition(@RequestBody Composition composition) {
        compositionRepository.save(composition);
        Resource<CompositionFullResource> resource = new Resource(new CompositionFullResource(composition));
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @ApiOperation("Gets all informations about a composition")
    @GetMapping(path = "/{compositionId}", produces = APPLICATION_HAL_JSON)
    HttpEntity<Resource<CompositionFullResource>> getComposition(@PathVariable Long compositionId) {

        Composition composition = compositionRepository.getOne(compositionId);
        //TODO List<org.springframework.core.io.Resource> files = storageService.loadManyAsResource(composition.getFilenames());
        Resource<CompositionFullResource> resource = new Resource(new CompositionFullResource(composition));
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @ApiOperation("Updates the tracks of a composition")
    @PutMapping(path = "/{compositionId}", produces = APPLICATION_HAL_JSON)
    HttpEntity<Resource<CompositionFullResource>> updateCompositionTracks(@PathVariable("compositionId") Long compositionId/*,
                                                                          @RequestPart("tracks") List<Track> tracks,*/) {

        Composition result = compositionRepository.getOne(compositionId);
        Resource<CompositionFullResource> resource = null;
        if (result != null) {
            //result.updateTracks(tracks); // TODO use a merge instead
            compositionRepository.save(result);//TODO replace with transaction save
            resource = new Resource<>(new CompositionFullResource(result));
        }
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }
}
