package com.klangfang.core.storage;

import com.cloudinary.Singleton;
import com.cloudinary.SingletonManager;
import com.cloudinary.utils.ObjectUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Component
public class SoundUploadComponent {

    private static final Logger LOG = LogManager.getLogger(SoundUploadComponent.class);

    private SingletonManager cloudinaryManager;

    @Value("${klangfang.cloudinary.base}")
    private String base;

    @Value("${klangfang.cloudinary.publicId}")
    private String publicId;


    @PostConstruct
    public void construct() {

        cloudinaryManager = new SingletonManager();
        cloudinaryManager.init();

        //uploadSound(Files.readAllBytes(Path.of("/home/casasky/Musik/sound_1.ogg")));


    }


    @PreDestroy
    public void destroy() {

        cloudinaryManager.destroy();

    }


    public static void main(String[] args) throws IOException {



        //SpringApplication.run(SoundUploadComponent.class, args);
        //SoundUploadComponent soundUploadComponent = new SoundUploadComponent();
        //soundUploadComponent.uploadSound(Files.readAllBytes(Path.of("/home/casasky/Bilder/1024px-DBSCAN-density-data.svg.png")));


    }

    public String uploadSound(byte[] soundBytes) {

        try {
            var soundKey = publicId + "/" + UUID.randomUUID().toString().replace("-","");//publicId + "/" + UUID.randomUUID().toString().replace("-","");
            Map params = ObjectUtils.asMap(
                    "public_id", soundKey,
                    //blebt so"folder", publicId,
                    "use_filename", false,
                    "resource_type", "raw"
            );

            Map upload = Singleton.getCloudinary().uploader().upload(soundBytes, params);

            return upload.get("secure_url").toString();

        } catch (IOException e) {

            LOG.error(e);
            throw new SoundUploadException();

        }

    }
}
