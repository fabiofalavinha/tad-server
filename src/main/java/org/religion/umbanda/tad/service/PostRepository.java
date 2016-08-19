package org.religion.umbanda.tad.service;

import org.religion.umbanda.tad.model.Archive;
import org.religion.umbanda.tad.model.Post;
import org.religion.umbanda.tad.model.VisibilityType;

import java.util.List;
import java.util.UUID;

public interface PostRepository {

    List<Post> findAll();

    List<Post> findPublishedPost(VisibilityType visibilityType);

    List<Post> findPublishedPost(VisibilityType visibilityType, int year, int month);

    List<Archive> findArchiveBy(VisibilityType visibilityType);

    Post findById(UUID postId);

    void removePostById(UUID postId);

    void createPost(Post post);

    void updatePost(Post post);

}
