package com.klangfang.core.service;

import com.klangfang.core.entities.Composition;
import com.klangfang.core.entities.Sound;
import com.klangfang.core.entities.type.Status;
import com.klangfang.core.exception.CompositionNotFoundException;
import com.klangfang.core.exception.MethodNotAllowedException;
import com.klangfang.core.request.CompositionRequest;
import com.klangfang.core.request.CompositionUpdateRequest;
import com.klangfang.core.request.SoundRequest;
import com.klangfang.core.response.CompositionOverview;
import com.klangfang.core.response.CompositionResponse;
import com.klangfang.core.storage.SoundUploadComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Component
@Transactional
public class CompositionService {

    private CompositionRepository repository;
    private final SoundUploadComponent soundUploadComponent;

    private static final Logger LOG = LogManager.getLogger(CompositionService.class);

    public CompositionService(CompositionRepository repository,
                              SoundUploadComponent soundUploadComponent) {
        this.repository = repository;
        this.soundUploadComponent = soundUploadComponent;

    }

    public CompositionOverview createComposition(CompositionRequest compositionRequest) {

        List<Sound> sounds = compositionRequest.sounds.stream()
                .map(s -> s.toEntity(soundUploadComponent.uploadSound(s.soundBytes)))
                .collect(Collectors.toList());

        if (sounds.isEmpty()) {
            throw new IllegalArgumentException("sounds list can not be empty");
        }

        Composition composition = new Composition(compositionRequest.title, compositionRequest.creatorName, sounds, 4);

        repository.save(composition);

        return CompositionOverview.build(composition);

    }


    public Set<CompositionOverview> loadCompositionsOverview(Integer page, Integer size) {
        Set<CompositionOverview> overviews = repository.findByStatusOrderByCreationTimeDesc(
                Status.AVAILABLE,
                PageRequest.of(page, size))
                .stream()
                .map(CompositionOverview::build)
                .collect(Collectors.toSet());

        return overviews;
    }

    public CompositionResponse update(Long id, CompositionUpdateRequest request, UpdateRequest updateRequest) {

        return updateRequest.isOpen() ? open(id) : updateRequest.isCancel() ? cancel(id) : join(id, request.sounds);

    }


    private CompositionResponse open(Long id) throws CompositionNotFoundException {

        Composition composition = repository.findById(id)
                .orElseThrow(() -> new CompositionNotFoundException(id));

        if (composition.isAvailable()) {
            composition.pick();
            return CompositionResponse.build(composition);
        }

        throw new MethodNotAllowedException("pick", composition.getStatus());

    }

    private CompositionResponse join(Long id, List<SoundRequest> newSounds) throws CompositionNotFoundException {

        Composition composition = repository.findById(id)
                .orElseThrow(() -> new CompositionNotFoundException(id));

        if (composition.isPicked()) {

            List<Sound> sounds = newSounds.stream()
                    .map(s -> s.toEntity(soundUploadComponent.uploadSound(s.soundBytes)))
                    .collect(Collectors.toList());

            composition.join(sounds);

            return CompositionResponse.build(composition);

        }

        throw new MethodNotAllowedException("release", composition.getStatus());

    }

    private CompositionResponse cancel(Long id) {

        Composition composition = repository.findById(id)
                .orElseThrow(() -> new CompositionNotFoundException(id));

        if (composition.isPicked()) {
            composition.cancel();
        }

        return CompositionResponse.build(composition);

    }

}