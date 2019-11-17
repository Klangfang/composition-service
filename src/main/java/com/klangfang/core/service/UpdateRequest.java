package com.klangfang.core.service;

public enum UpdateRequest {

    OPEN, CANCEL, JOIN;

    public boolean isOpen() {
        return name() == OPEN.name();
    }

    public boolean isCancel() {
        return name() == CANCEL.name();
    }

    public boolean isJoin() {
        return name() == JOIN.name();
    }


}
