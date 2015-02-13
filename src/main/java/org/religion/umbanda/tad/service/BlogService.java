package org.religion.umbanda.tad.service;

import java.util.List;
import org.religion.umbanda.tad.model.Archive;
import org.religion.umbanda.tad.service.vo.PostResponse;

public interface BlogService {

    List<Archive> getArchives(String visibility);
    List<PostResponse> findPublishedPostByVisibility(String visibilityName);
    List<PostResponse> findPostByArchive(String visibility, int year, int month);
    
}