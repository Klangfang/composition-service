package com.klangfang.core.storage;

import com.klangfang.core.entities.Composition;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface StorageService {


    void storeMultiPart(Composition composition, List<MultipartFile> files);

    //List<String> store(Composition composition);

    Resource loadFileAsResource(Long compositionId, String filename);

    Stream<Path> loadMany(Long compositionId, List<String> filenames);

    List<Resource> loadManyAsResource(Long compositionId, List<String> filenames);
}
