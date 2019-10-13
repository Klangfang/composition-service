package com.klangfang.core.common.type;

public enum AudioFileType {

    THREE_GP(".3gp");

    String name;

    AudioFileType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
