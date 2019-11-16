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

    private SingletonManager cloudinaryManager;

    @Value("${klangfang.cloudinary.base}")
    private String base;

    @Value("${klangfang.cloudinary.publicId}")
    private String publicId;

    //@Value("${klangfang.cloudinary.cloudName}")
    private String cloudName;

    //@Value("${klangfang.cloudinary.apiKey}")
    private String apiKey;

    //@Value("${klangfang.cloudinary.apiSecret}")
    private String apiSecret;

    private static final Logger LOG = LogManager.getLogger(SoundUploadComponent.class);


    @PostConstruct
    public void construct() throws IOException {

        //Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
          //      "cloud_name", cloudName,
            //            "api_key", apiKey,
              //          "api_secret", apiSecret));

        cloudinaryManager = new SingletonManager();
        //cloudinaryManager.setCloudinary(cloudinary);
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
