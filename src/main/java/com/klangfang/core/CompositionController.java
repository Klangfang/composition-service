package com.klangfang.core;

import com.klangfang.core.entities.Composition;
import com.klangfang.core.entities.Sound;
import com.klangfang.core.exception.CompositionNotFoundException;
import com.klangfang.core.response.CompositionOverview;
import com.klangfang.core.response.CompositionResponse;
import com.klangfang.core.response.OverviewResponse;
import com.klangfang.core.service.CompositionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

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

    @GetMapping(value = "/{id}/{fileName:.+}")
    public ResponseEntity<org.springframework.core.io.Resource> downloadFile(@PathVariable Long id,
                                                                             @PathVariable String fileName,
                                                                             HttpServletRequest request) {

        System.out.println("Download klappt!");
        org.springframework.core.io.Resource resource = service.downloadFile(fileName, id, request);
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
    ResponseEntity<OverviewResponse> compositionsOverview(@RequestParam Integer page,
                                                          @RequestParam Integer size) {


        Set<CompositionOverview> overviews = service.loadCompositionsOverview(page, size);

        if (overviews.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(new OverviewResponse(overviews,
                linkTo(methodOn(CompositionController.class).compositionsOverview(++page, size)).toString()));
    }

    @ApiOperation("Creates a new composition")
    @PostMapping(produces = APPLICATION_HAL_JSON)
    ResponseEntity<CompositionOverview> newCompositionMultipart(@RequestPart("composition") Composition composition,
                                                                 @RequestParam("files") MultipartFile[] files) {

        composition.release();

        CompositionOverview overview = service.createCompositionMultipart(composition, files);

        return ResponseEntity
                .created(linkTo(methodOn(CompositionController.class).pick(composition.getId())).toUri())
                .body(overview);
    }

   /* @ApiOperation("Creates a new composition")
    @PostMapping(produces = APPLICATION_HAL_JSON)
    ResponseEntity<CompositionOverview> newComposition(@RequestBody Composition composition) {

        composition.release();

        CompositionOverview overview = service.createComposition(composition);

        return ResponseEntity
                .created(linkTo(methodOn(CompositionController.class).pick(composition.getId())).toUri())
                .body(overview);
    }*/

    @ApiOperation("Picks a composition and returns all information about it")
    @PutMapping(path = "/{id}/pick", produces = APPLICATION_HAL_JSON)
    ResponseEntity<CompositionResponse> pick(@PathVariable Long id) {

        try {

            return ResponseEntity.ok(service.pick(id));

        } catch (CompositionNotFoundException e) {

            return ResponseEntity.unprocessableEntity().build();

        }

    }

    @ApiOperation("Makes the composition available and adds new sounds to it")
    @PutMapping(path = "/{id}/release", produces = APPLICATION_HAL_JSON)
    ResponseEntity<CompositionOverview> release(@PathVariable Long id,
                                                          @RequestPart("sounds") List<Sound> sounds,
                                                          @RequestParam("files") MultipartFile[] files) {

        try {

            return ResponseEntity.ok(service.release(id, sounds, files));

        } catch (CompositionNotFoundException e) {

            return ResponseEntity.unprocessableEntity().build();

        }
    }

    //@PutMapping(path = "/{id}/release", produces = APPLICATION_HAL_JSON)
    ResponseEntity<CompositionOverview> release(@PathVariable Long id) {

        try {

            return ResponseEntity.ok(service.release(id));

        } catch (CompositionNotFoundException e) {

            return ResponseEntity.unprocessableEntity().build();

        }
    }
}
