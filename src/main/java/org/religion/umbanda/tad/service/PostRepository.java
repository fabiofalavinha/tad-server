package org.religion.umbanda.tad.service;

import org.religion.umbanda.tad.model.Archive;
import org.religion.umbanda.tad.model.Post;
import org.religion.umbanda.tad.model.PostPageable;
import org.religion.umbanda.tad.model.VisibilityType;

import java.util.List;
import java.util.UUID;

public interface PostRepository {

    List<Post> findAll();

    PostPageable findPublishedPost(VisibilityType visibilityType, int pageNumber);

    PostPageable findPublishedPost(VisibilityType visibilityType, int year, int month, int pageNumber);

    List<Archive> findArchiveBy(VisibilityType visibilityType);

    Post findById(UUID postId);

    void removePostById(UUID postId);

    void createPost(Post post);

    void updatePost(Post post);

}
