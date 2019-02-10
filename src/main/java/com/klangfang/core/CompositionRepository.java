package com.klangfang.core;

import com.klangfang.core.entities.Composition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompositionRepository extends JpaRepository<Composition, Long> {

    /**
     * Finds all compositions of a certain creator
     * @param creatorname must be not null
     * @return a list a created compositions
     */
    List<Composition> findByCreatorname(String creatorname);

    /**
     * Find all compositions by a certain status
     * @param status
     * @return list of composition having the given status
     */
    List<Composition> findByStatus(Status status);
}
