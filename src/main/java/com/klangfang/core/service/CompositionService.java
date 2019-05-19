package com.klangfang.core.service;

import com.klangfang.core.entities.Composition;
import com.klangfang.core.entities.Sound;
import com.klangfang.core.entities.type.Status;
import com.klangfang.core.exception.CompositionNotFoundException;
import com.klangfang.core.exception.MethodNotAllowedException;
import com.klangfang.core.response.CompositionOverview;
import com.klangfang.core.response.CompositionResponse;
import com.klangfang.core.ResponseTransformer;
import com.klangfang.core.storage.StorageService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Component
@Transactional
public class CompositionService {

    private CompositionRepository repository;
    private ResponseTransformer transformer;
    private final StorageService storage;

    public CompositionService(CompositionRepository repository,
                              ResponseTransformer transformer,
                              StorageService storage) {
        this.repository = repository;
        this.transformer = transformer;
        this.storage = storage;
    }

    public CompositionOverview createComposition(Composition composition, MultipartFile[] files) {

        repository.save(composition);
        storage.store(composition.getId(), List.of(files));
        return transformer.overview(composition);
    }

    public Set<CompositionOverview> loadCompositionsOverview(Integer page, Integer size) {
        Set<CompositionOverview> overviews = repository.findByStatus(
                Status.valueOf(Status.AVAILABLE.name()),
                PageRequest.of(page, size))
                .stream()
                .map(transformer::overview)
                .collect(Collectors.toSet());

        return overviews;
    }

    public CompositionResponse pick(Long id) throws CompositionNotFoundException {

        Composition composition = repository.findById(id)
                .orElseThrow(() -> new CompositionNotFoundException(id));

        if (composition.getStatus() == Status.AVAILABLE) {
            composition.pick();
            return transformer.fullResponse(composition);
        }

        throw new MethodNotAllowedException("pick", composition.getStatus().name());
    }

    public CompositionOverview release(Long id, List<Sound> newSounds, MultipartFile[] files)
            throws CompositionNotFoundException {

        Composition composition = repository.findById(id)
                .orElseThrow(() -> new CompositionNotFoundException(id));

        if (composition.getStatus() == Status.PICKED) {
            composition.addSounds(newSounds);
            storage.store(id, Arrays.asList(files));
            return transformer.overview(composition);
        }

        throw new MethodNotAllowedException("release", composition.getStatus().name());
    }

    public org.springframework.core.io.Resource downloadFile(String fileName, Long compositionId, HttpServletRequest request) {
        // Load file as Resource
        org.springframework.core.io.Resource resource = storage.loadFileAsResource(compositionId, fileName);

        return resource;
    }

}