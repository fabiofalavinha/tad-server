package org.religion.umbanda.tad.service.impl;

import org.religion.umbanda.tad.model.ImageCarouselPathConfiguration;
import org.religion.umbanda.tad.service.ImageCarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
public class ImageCarouselServiceImpl implements ImageCarouselService {

    @Autowired
    private ImageCarouselPathConfiguration imageCarouselPathConfiguration;

    @RequestMapping(value="/upload", method= RequestMethod.POST)
    @Override
    public void uploadImage(
        @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Não foi possivel realizar o upload da imagem: arquivo inválido (vazio)");
        }
        try {
            final Path imageCarouselLocalPath = imageCarouselPathConfiguration.getLocalPath();
            final Path newImageCarouselPath = Paths.get(imageCarouselLocalPath.toString(), UUID.randomUUID().toString() + ".jpg");
            final byte[] bytes = file.getBytes();
            BufferedOutputStream stream = null;
            try {
                stream = new BufferedOutputStream(new FileOutputStream(newImageCarouselPath.toFile()));
                stream.write(bytes);
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível efetivar o download da imagem", e);
        }
    }

    @RequestMapping(value = "/carousel", method = RequestMethod.GET)
    @Override
    public String[] getImageNames() {
        final Path imageCarouselLocalPath = imageCarouselPathConfiguration.getLocalPath();
        final File[] images = imageCarouselLocalPath.toFile().listFiles();
        final String[] imageNames = new String[images.length];
        for (int i = 0; i < images.length; ++i) {
            imageNames[i] = images[i].getName();
        }
        return imageNames;
    }

}
