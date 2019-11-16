package com.klangfang.core;

import com.klangfang.core.exception.CompositionNotFoundException;
import com.klangfang.core.request.CompositionRequest;
import com.klangfang.core.request.CompositionUpdateRequest;
import com.klangfang.core.response.CompositionOverview;
import com.klangfang.core.response.CompositionResponse;
import com.klangfang.core.response.OverviewResponse;
import com.klangfang.core.service.CompositionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.util.CollectionUtils.isEmpty;

@RestController
@RequestMapping("compositions")
class CompositionController {

    private final CompositionService service;

    private static final Logger LOG = LogManager.getLogger(CompositionController.class);


    @Autowired
    public CompositionController(CompositionService service) {
        this.service = service;
    }


    @GetMapping(path = "/compositionsOverview", params = {"page", "size"})
    ResponseEntity<OverviewResponse> compositionsOverview(@RequestParam Integer page,
                                                          @RequestParam Integer size) {


        Set<CompositionOverview> overviews = service.loadCompositionsOverview(page, size);

        if (overviews.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(new OverviewResponse(overviews,
                linkTo(methodOn(CompositionController.class).compositionsOverview(++page, size)).toString()));
    }


    @PostMapping
    ResponseEntity<CompositionOverview> createComposition(@RequestBody CompositionRequest composition) {

        CompositionOverview overview = service.createComposition(composition);

        CompositionUpdateRequest request = new CompositionUpdateRequest();

        return ResponseEntity
                .created(linkTo(methodOn(CompositionController.class).update(overview.id, request)).toUri())
                .body(overview);

    }


    @PutMapping("/{id}")
    ResponseEntity<CompositionResponse> update(@PathVariable Long id, @RequestBody CompositionUpdateRequest request) {

        try {

            LOG.debug("----> " + request.sounds);
            return ResponseEntity.ok(isEmpty(request.sounds) ? service.pick(id) : service.release(id, request.sounds));

        } catch (CompositionNotFoundException e) {

            return ResponseEntity.unprocessableEntity().build();

        }

    }

}
