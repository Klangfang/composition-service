package com.klangfang.core.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface StorageService {


    List<String> store(List<MultipartFile> files);

    Stream<Path> loadMany(List<String> filenames);

    List<Resource> loadManyAsResource(List<String> filenames);
}
