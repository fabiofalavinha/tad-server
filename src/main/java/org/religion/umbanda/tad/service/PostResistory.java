package org.religion.umbanda.tad.service;

import org.religion.umbanda.tad.model.Archive;
import org.religion.umbanda.tad.model.Post;
import org.religion.umbanda.tad.model.VisibilityType;

import java.util.List;

public interface PostResistory {

    List<Post> findPublishedPost(VisibilityType visibilityType);
    List<Archive> getArchives();

}
