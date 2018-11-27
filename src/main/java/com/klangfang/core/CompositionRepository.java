package com.klangfang.core;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompositionRepository extends JpaRepository<Composition, Long> {

    /**
     * Finds all compositions of a certain creator
     * @param creatorname must be not null
     * @return a list a created compositions
     */
    List<Composition> findAllByCreatorname(String creatorname);
}
