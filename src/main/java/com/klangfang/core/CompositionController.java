package com.klangfang.core;

import com.klangfang.core.entities.Composition;
import com.klangfang.core.entities.Track;
import com.klangfang.core.repositories.CompositionRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.*;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Api(description = "Set of endpoints for managing composition data.")
@RestController
@RequestMapping("/compositions")
class CompositionController {

    private final CompositionRepository compositionRepository;
    private final CompositionResourceAssembler assembler;

    @Autowired
    public CompositionController(CompositionRepository compositionRepository,
                                 CompositionResourceAssembler assembler) {
        this.compositionRepository = compositionRepository;
        this.assembler = assembler;
    }

    @ApiOperation("Gets a list of composition overviews")
    @GetMapping(path = "/compositionsOverview", params = {"page", "size"})
    Resources<Resource<CompositionOverviewDto>> compositionsOverview(@RequestParam Integer page,
                                                                  @RequestParam Integer size) {

        List<Resource<CompositionOverviewDto>> compositions = compositionRepository.findByStatus(
                Status.valueOf(Status.RELEASED.name()),
                PageRequest.of(page, size))
                .stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

       return new Resources<>(compositions,
               linkTo(methodOn(CompositionController.class).compositionsOverview(++page, size)).withSelfRel());
    }

    @ApiOperation("Creates a new composition")
    @PostMapping
    ResponseEntity<Resource<CompositionOverviewDto>> newComposition(@RequestPart("composition") Composition composition,
                                                                    @RequestParam("files") MultipartFile[] files) {

        for (MultipartFile file : files) {
            //TODO save to file
        }

        composition.release();
        compositionRepository.save(composition);

        return ResponseEntity
                .created(linkTo(methodOn(CompositionController.class).pick(composition.getId())).toUri())
                .body(assembler.toResource(composition));
    }

    @ApiOperation("Picks a composition and returns all information about it")
    @PutMapping(path = "/{id}/pick")
    ResponseEntity<Resource<Composition>> pick(@PathVariable Long id) {

        Composition composition = compositionRepository.findById(id)
                .orElseThrow(() -> new CompositionNotFoundException(id));

        if (composition.getStatus() == Status.RELEASED) {
            composition.pick();
            return ResponseEntity.ok(assembler.toFullResource(compositionRepository.save(composition)));
        }

        throw new MethodNotAllowedException("pick", composition.getStatus().name());
    }

    @ApiOperation("Released the composition and updates its tracks")
    @PutMapping(path = "/{id}/release")
    ResponseEntity<Resource<CompositionOverviewDto>> release(@PathVariable Long id,
                                        @RequestPart("tracks") List<Track> newTracks,
                                        @RequestParam("files") MultipartFile[] files) {

        Composition composition = compositionRepository.findById(id)
                .orElseThrow(() -> new CompositionNotFoundException(id));

        if (composition.getStatus() == Status.PICKED) {
            composition.updateTracksAndRelease(newTracks);
            for (MultipartFile file : files) {
                //TODO save to file
            }
            return ResponseEntity.ok(assembler.toResource(compositionRepository.save(composition)));
        }

        throw new MethodNotAllowedException("release", composition.getStatus().name());

    }
}
