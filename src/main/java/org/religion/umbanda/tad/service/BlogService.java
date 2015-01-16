package org.religion.umbanda.tad.service;

import java.util.List;
import org.religion.umbanda.tad.model.Archive;
import org.religion.umbanda.tad.model.PostType;
import org.religion.umbanda.tad.model.Post;

public interface BlogService {

    List<Archive> getArchives();
    List<Post> findPostBy(String postType);
    
}