package org.religion.umbanda.tad.service.impl;

import java.util.List;
import java.util.ArrayList;

import org.religion.umbanda.tad.model.Archive;
import org.religion.umbanda.tad.model.Post;
import org.religion.umbanda.tad.model.PostType;
import org.religion.umbanda.tad.model.VisibilityType;
import org.religion.umbanda.tad.service.BlogService;

import org.religion.umbanda.tad.service.PostResistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogServiceImpl implements BlogService {

    @Autowired
    private PostResistory postResistory;

    @RequestMapping("/postArchives")
    @Override
    public List<Archive> getArchives() {
        return postResistory.getArchives();
    }
    
    @RequestMapping("/publishedPosts/{visibility}")
    @Override
    public List<Post> findPublishedPostByVisibility(
        @PathVariable("visibility") String visibility) {
        VisibilityType visibilityType;
        try {
            visibilityType = VisibilityType.valueOf(visibility);
        } catch (IllegalArgumentException e) {
            return new ArrayList<Post>();
        }
        return postResistory.findPublishedPost(visibilityType);
    }
    
}