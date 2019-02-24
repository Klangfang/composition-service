package com.klangfang.core;

public final class FilePathUtils {

    public static String generateSoundFilePath(String streamServerUri, Long compositionId, String filename) {

        return compositionId + "/" + compositionId + "/" + filename;
    }
}
