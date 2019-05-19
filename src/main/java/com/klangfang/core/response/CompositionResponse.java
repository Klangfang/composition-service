package com.klangfang.core.response;

import com.klangfang.core.entities.Composition;

public class CompositionResponse {

    public final Composition composition;

    public final String pickUrl;

    public CompositionResponse(Composition composition, String pickUrl) {
        this.composition = composition;
        this.pickUrl = pickUrl;
    }

    public CompositionResponse(Composition composition) {
        this.composition = composition;
        this.pickUrl = null;
    }

}
