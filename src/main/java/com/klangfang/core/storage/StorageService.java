package com.klangfang.core.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface StorageService {


    List<String> store(Long compositionId, List<MultipartFile> files);

    Resource loadFileAsResource(Long compositionId, String filename);

    Stream<Path> loadMany(Long compositionId, List<String> filenames);

    List<Resource> loadManyAsResource(Long compositionId, List<String> filenames);
}
