package com.klangfang.core;

public class MethodNotAllowedException extends RuntimeException {

    public MethodNotAllowedException(String action, String status) {
        super("You can't " + action + " a composition that is in the " + status + " status");
    }
}
