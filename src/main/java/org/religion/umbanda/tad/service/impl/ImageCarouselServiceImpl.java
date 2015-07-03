package org.religion.umbanda.tad.service.impl;

import org.religion.umbanda.tad.model.ImageCarouselPathConfiguration;
import org.religion.umbanda.tad.service.ImageCarouselService;
import org.religion.umbanda.tad.service.vo.CarouselImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
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
            throw new IllegalStateException("Não foi possível efetivar o upload da imagem", e);
        }
    }

    @RequestMapping(value = "/carousel", method = RequestMethod.GET)
    @Override
    public List<CarouselImage> getImages() {
        final List<CarouselImage> carouselImages = new ArrayList<CarouselImage>();
        final Path imageCarouselLocalPath = imageCarouselPathConfiguration.getLocalPath();
        final File[] images = imageCarouselLocalPath.toFile().listFiles();
        if (images != null) {
            Arrays.sort(images, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    final long t1 = o1.lastModified();
                    final long t2 = o2.lastModified();
                    return t1 > t2 ? 1 : t1 == t2 ? 0 : -1;
                }
            });
            for (File image : images) {
                carouselImages.add(new CarouselImage(image.getName()));
            }
        }
        return carouselImages;
    }

    @RequestMapping(value = "/carousel/{name}", method = RequestMethod.DELETE)
    @Override
    public void removeImage(
        @PathVariable("name") String name) {
        System.out.printf("Requesting to delete image [%s]...\n", name);
        final Path imagePath = imageCarouselPathConfiguration.getLocalPath().resolve(name);
        System.out.printf("Image path: %s\n", imagePath.toString());
        final File imageFilePath = imagePath.toFile();
        if (imageFilePath.exists()) {
            imageFilePath.delete();
        } else {
            System.out.printf("File does not exists: %s\n", imageFilePath.toString());
        }
    }

}
