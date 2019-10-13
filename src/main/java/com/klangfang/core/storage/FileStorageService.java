package com.klangfang.core.storage;

import com.klangfang.core.entities.Composition;
import com.klangfang.core.entities.Sound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

        createDirectories(fileStorageLocation);
    }

    private void createDirectories(Path path) {
        try {
            Files.createDirectories(path);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public void storeMultiPart(Composition composition, List<MultipartFile> files) {

        if (composition.getSounds().size() != files.size() || files.isEmpty()) {
            throw new IllegalArgumentException("Not all sounds has been uploaded");
        }
        System.out.println("SIZE: " + files.size());
        System.out.println(files.toString());

        List<String> filenames = new ArrayList<>();
        createDirectories(Paths.get(fileStorageLocation+"/"+composition.getId()));
        int soundPosition = 0;
        String title = "";
        for (MultipartFile file : files) {
            // Normalize file name
            //String filename = StringUtils.cleanPath(composition.getSounds().get(soundPosition).getTitle());

            try {
                // Check if the file's name contains invalid characters
                /*if(filename.contains("..")) {
                    throw new FileStorageException("Sorry! Filename contains invalid path sequence " + filename);
                }*/

                // Copy file to the target location (Replacing existing file with the same name)
                title = composition.getSoundTile(soundPosition);
                soundPosition++;
                Path targetLocation = this.fileStorageLocation.resolve(composition.getId()+"/"+title);
                System.out.println("Storing in.." + targetLocation.toString());
                /*try (FileOutputStream stream = new FileOutputStream(targetLocation.toString())) {
                    stream.write(file.getBytes());
                }*/
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                filenames.add(title);
            } catch (IOException ex) {
                throw new FileStorageException("Could not store file " + title + ". Please try again!", ex);
            }
        }

    }

   /* @Override
    public List<String> store(Composition composition) {

        List<String> filenames = new ArrayList<>();
        Long compositionId = composition.getId();
        createDirectories(Paths.get(fileStorageLocation+"/"+compositionId));
        for (Sound sound : composition.getSounds()) {
            // Normalize file name
            String filename = StringUtils.cleanPath(sound.getTitle());
            try {
                // Check if the file's name contains invalid characters
                if(filename.contains("..")) {
                    throw new FileStorageException("Sorry! Filename contains invalid path sequence " + filename);
                }
                // Copy file to the target location (Replacing existing file with the same name)
                Path targetLocation = this.fileStorageLocation.resolve(compositionId+"/"+filename);
                try (FileOutputStream stream = new FileOutputStream(targetLocation.toString())) {
                    stream.write(sound.getContent());
                }
                //Files.copy(sound.getContent(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                filenames.add(filename);
            } catch (IOException ex) {
                throw new FileStorageException("Could not store file " + filename + ". Please try again!", ex);
            }
        }

        return filenames;

    }*/

    @Override
    public Resource loadFileAsResource(Long compositionId, String filename) {
        Resource resource;
        try {
            Path filePath = this.fileStorageLocation.resolve(compositionId+"/"+filename).normalize();
            resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " +filename);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + filename, ex);
        }
    }

    @Override
    public Stream<Path> loadMany(Long compositionId, List<String> filenames) {
        return null;
    }

    @Override
    public List<Resource> loadManyAsResource(Long compositionId, List<String> filenames) {
        List<Resource> files = new ArrayList<>();
        for (String filename : filenames) {
            try {
                Path filePath = this.fileStorageLocation.resolve(compositionId+"/"+filename).normalize();
                Resource resource = new UrlResource(filePath.toUri());
                if(resource.exists()) {
                    files.add(resource);
                } else {
                    throw new MyFileNotFoundException("File not found " +filename);
                }
            } catch (MalformedURLException ex) {
                throw new MyFileNotFoundException("File not found " + filename, ex);
            }
        }

        return files;
    }
}
