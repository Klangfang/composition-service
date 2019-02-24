package com.klangfang.core;

import com.klangfang.core.entities.Composition;
import org.springframework.hateoas.ResourceSupport;

public class CompositionDto extends ResourceSupport {

    private Composition composition;

    public CompositionDto(Composition composition) {
        this.composition = composition;
    }

    public Composition getComposition() {
        return composition;
    }
}
