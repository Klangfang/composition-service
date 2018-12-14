package com.klangfang.core.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Component
public class FileStorageService implements StorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public List<String> store(List<MultipartFile> files) {
        List<String> filenames = new ArrayList<>();

        for (MultipartFile file : files) {
            // Normalize file name
            String filename = StringUtils.cleanPath(file.getOriginalFilename());

            try {
                // Check if the file's name contains invalid characters
                if(filename.contains("..")) {
                    throw new FileStorageException("Sorry! Filename contains invalid path sequence " + filename);
                }

                // Copy file to the target location (Replacing existing file with the same name)
                Path targetLocation = this.fileStorageLocation.resolve(filename);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                 filenames.add(filename);
            } catch (IOException ex) {
                throw new FileStorageException("Could not store file " + filename + ". Please try again!", ex);
            }
        }

        return filenames;
    }

    @Override
    public Stream<Path> loadMany(List<String> filenames) {
        return null;
    }

    @Override
    public List<Resource> loadManyAsResource(List<String> filenames) {
        List<Resource> files = new ArrayList<>();
        for (String filename : filenames) {
            try {
                Path filePath = this.fileStorageLocation.resolve(filename).normalize();
                Resource resource = new UrlResource(filePath.toUri());
                if(resource.exists()) {
                    files.add(resource);
                } else {
                    throw new MyFileNotFoundException("File not found " + filename);
                }
            } catch (MalformedURLException ex) {
                throw new MyFileNotFoundException("File not found " + filename, ex);
            }
        }

        return files;
    }
}
