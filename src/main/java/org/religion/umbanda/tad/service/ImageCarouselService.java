package org.religion.umbanda.tad.service;

import org.religion.umbanda.tad.service.vo.CarouselImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageCarouselService {

    List<CarouselImage> getImages();

    void uploadImage(MultipartFile file);

    void removeImage(String name);

}
