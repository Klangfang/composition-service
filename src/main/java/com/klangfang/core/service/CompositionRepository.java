package com.klangfang.core.service;

import com.klangfang.core.entities.type.Status;
import com.klangfang.core.entities.Composition;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompositionRepository extends JpaRepository<Composition, Long> {

    /**
     * Finds all compositions of a certain creator
     * @param creatorName must be not null
     * @return a list a created compositions
     */
    List<Composition> findByCreatorName(String creatorName);

    /**
     * Find all compositions by a certain status
     * @param status
     * @return list of composition having the given status
     */
    List<Composition> findByStatusOrderByCreationTimeDesc(Status status, Pageable pageable);

    /**
     *
     * @param title
     * @return
     */
    Optional<Composition> findByTitle(String title);
}
