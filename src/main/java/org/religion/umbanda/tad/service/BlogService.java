package org.religion.umbanda.tad.service;

import org.religion.umbanda.tad.model.Archive;
import org.religion.umbanda.tad.service.vo.PostResponse;

import java.util.List;

public interface BlogService {

    List<Archive> getArchives(String visibility);
    List<PostResponse> findPublishedPostByVisibility(String visibilityName);
    List<PostResponse> findPostByArchive(String visibility, int year, int month);
    List<PostResponse> getPostsByUserId(String userId);
    void removePost(String id);
    
}