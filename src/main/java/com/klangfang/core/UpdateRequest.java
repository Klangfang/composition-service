package com.klangfang.core;

public enum UpdateRequest {

    PICK, RELEASE;

    public boolean isPick() {
        return name() == PICK.name();
    }

    public boolean isRelease() {
        return name() == RELEASE.name();
    }



}
