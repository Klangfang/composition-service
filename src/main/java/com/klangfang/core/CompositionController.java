package com.klangfang.core;

import com.klangfang.core.entities.Composition;
import com.klangfang.core.entities.Sound;
import com.klangfang.core.repositories.CompositionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Api(description = "Set of endpoints for managing composition data.")
@RestController
@RequestMapping("/compositions")
class CompositionController {

    private final CompositionService service;

    private final String APPLICATION_HAL_JSON = "application/hal+json";

    @Autowired
    public CompositionController(CompositionService service) {
        this.service = service;
    }

    @GetMapping(value = "/{fileName:.+}", params = "compositionId")
    public ResponseEntity<org.springframework.core.io.Resource> downloadFile(@PathVariable String fileName,
                                                 @RequestParam Long compositionId,
                                                 HttpServletRequest request) {

        org.springframework.core.io.Resource resource = service.downloadFile(fileName, compositionId, request);
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
    Resources<Resource<CompositionOverview>> compositionsOverview(@RequestParam Integer page,
                                                                  @RequestParam Integer size) {

       return new Resources<>(service.loadCompositionsOverview(page, size),
               linkTo(methodOn(CompositionController.class).compositionsOverview(++page, size)).withSelfRel());
    }

    @ApiOperation("Creates a new composition")
    @PostMapping(produces = APPLICATION_HAL_JSON)
    ResponseEntity<Resource<CompositionOverview>> newComposition(@RequestPart("composition") Composition composition,
                                                                 @RequestParam("files") MultipartFile[] files) {

        composition.release();
        Resource<CompositionOverview> compositionOverview = service.createComposition(composition, files);
        return ResponseEntity
                .created(linkTo(methodOn(CompositionController.class).pick(composition.getId())).toUri())
                .body(compositionOverview);
    }

    @ApiOperation("Picks a composition and returns all information about it")
    @PutMapping(path = "/{id}/pick", produces = APPLICATION_HAL_JSON)
    ResponseEntity<Resource<Composition>> pick(@PathVariable Long id) {

       return ResponseEntity.ok(service.pick(id));
    }

    @ApiOperation("Makes the composition available and adds new sounds to it")
    @PutMapping(path = "/{id}/release", produces = APPLICATION_HAL_JSON)
    ResponseEntity<Resource<CompositionOverview>> release(@PathVariable Long id,
                                                          @RequestPart("sounds") List<Sound> sounds,
                                                          @RequestParam("files") MultipartFile[] files) {

        return ResponseEntity.ok(service.release(id, sounds, files));
    }
}
