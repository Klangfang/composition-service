package com.klangfang.core;

import com.klangfang.core.entities.Composition;
import com.klangfang.core.entities.Track;
import com.klangfang.core.repositories.CompositionRepository;
import com.klangfang.core.storage.StorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.*;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
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
    private final StorageService storageService;

    private final String APPLICATION_HAL_JSON = "application/hal+json";

    @Autowired
    public CompositionController(CompositionRepository compositionRepository,
                                 CompositionResourceAssembler assembler,
                                 StorageService storageService) {
        this.compositionRepository = compositionRepository;
        this.assembler = assembler;
        this.storageService = storageService;
    }

    @GetMapping(value = "/{fileName:.+}", params = "compositionId")
    public ResponseEntity<org.springframework.core.io.Resource> downloadFile(@PathVariable String fileName,
                                                 @RequestParam Long compositionId,
                                                 HttpServletRequest request) {
        // Load file as Resource
        org.springframework.core.io.Resource resource = storageService.loadFileAsResource(compositionId, fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
           // logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @ApiOperation("Gets a list of composition overviews")
    @GetMapping(path = "/compositionsOverview", params = {"page", "size"}, produces = APPLICATION_HAL_JSON)
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
    @PostMapping(produces = APPLICATION_HAL_JSON)
    ResponseEntity<Resource<CompositionOverviewDto>> newComposition(@RequestPart("composition") Composition composition,
                                                                    @RequestParam("files") MultipartFile[] files) {

        composition.release();
        compositionRepository.save(composition);
        storageService.store(composition.getId(), Arrays.asList(files));

        return ResponseEntity
                .created(linkTo(methodOn(CompositionController.class).pick(composition.getId())).toUri())
                .body(assembler.toResource(composition));
    }

    @ApiOperation("Picks a composition and returns all information about it")
    @PutMapping(path = "/{id}/pick", produces = APPLICATION_HAL_JSON)
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
    @PutMapping(path = "/{id}/release", produces = APPLICATION_HAL_JSON)
    ResponseEntity<Resource<CompositionOverviewDto>> release(@PathVariable Long id,
                                        @RequestPart("tracks") List<Track> newTracks,
                                        @RequestParam("files") MultipartFile[] files) {

        Composition composition = compositionRepository.findById(id)
                .orElseThrow(() -> new CompositionNotFoundException(id));

        if (composition.getStatus() == Status.PICKED) {
            composition.updateTracksAndRelease(newTracks);
            storageService.store(id, Arrays.asList(files));
            return ResponseEntity.ok(assembler.toResource(composition));
        }

        throw new MethodNotAllowedException("release", composition.getStatus().name());

    }
}
