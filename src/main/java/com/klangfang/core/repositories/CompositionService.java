package com.klangfang.core.repositories;

import com.klangfang.core.*;
import com.klangfang.core.entities.Composition;
import com.klangfang.core.entities.Sound;
import com.klangfang.core.storage.StorageService;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
public class CompositionService {

    private CompositionRepository repository;
    private CompositionResourceAssembler assembler;
    private final StorageService storage;

    public CompositionService(CompositionRepository repository,
                              CompositionResourceAssembler assembler,
                              StorageService storage) {
        this.repository = repository;
        this.assembler = assembler;
        this.storage = storage;
    }

    public Resource<CompositionOverview> createComposition(Composition composition, MultipartFile[] files) {

        repository.save(composition);
        storage.store(composition.getId(), List.of(files));
        return assembler.toResource(composition);
    }

    public List<Resource<CompositionOverview>> loadCompositionsOverview(Integer page, Integer size) {
        List<Resource<CompositionOverview>> compositions = repository.findByStatus(
                Status.valueOf(Status.AVAILABLE.name()),
                PageRequest.of(page, size))
                .stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return compositions;
    }

    public Resource<Composition> pick(Long id) {
        Composition composition = repository.findById(id)
                .orElseThrow(() -> new CompositionNotFoundException(id));

        if (composition.getStatus() == Status.AVAILABLE) {
            composition.pick();
            return assembler.toFullResource(composition);
        }

        throw new MethodNotAllowedException("pick", composition.getStatus().name());
    }

    public Resource<CompositionOverview> release(Long id, List<Sound> newSounds, MultipartFile[] files) {
        Composition composition = repository.findById(id)
                .orElseThrow(() -> new CompositionNotFoundException(id));

        if (composition.getStatus() == Status.PICKED) {
            composition.addSounds(newSounds);
            storage.store(id, Arrays.asList(files));
            return assembler.toResource(composition);
        }

        throw new MethodNotAllowedException("release", composition.getStatus().name());
    }

    public org.springframework.core.io.Resource downloadFile(String fileName, Long compositionId, HttpServletRequest request) {
        // Load file as Resource
        org.springframework.core.io.Resource resource = storage.loadFileAsResource(compositionId, fileName);

        return resource;
    }
}