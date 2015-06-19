package org.religion.umbanda.tad.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.File;
import java.nio.file.Path;

@Configuration
@PropertySource("classpath:application.properties")
public class ImageCarouselPathConfiguration {

    @Value("${blog.home.carousel.imagePath}")
    private String localPath;

    public Path getLocalPath() {
        if (localPath == null || "".equals(localPath.trim())) {
            throw new IllegalArgumentException(
                "Image carousel local path was not configured. Please, check 'application.properties' and set property 'blog.home.carousel.imagePath'");
        }
        return new File(localPath).getAbsoluteFile().toPath();
    }

}
