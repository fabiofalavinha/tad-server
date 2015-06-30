package org.religion.umbanda.tad.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageCarouselService {

    String[] getImageNames();
    void uploadImage(MultipartFile file);

}
