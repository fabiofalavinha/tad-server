package org.religion.umbanda.tad.service.impl;

import java.util.List;
import java.util.ArrayList;

import org.religion.umbanda.tad.model.Archive;
import org.religion.umbanda.tad.model.Post;
import org.religion.umbanda.tad.model.PostType;
import org.religion.umbanda.tad.service.BlogService;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogServiceImpl implements BlogService {

    @RequestMapping("/archives")
    public List<Archive> getArchives() {
        return new ArrayList<Archive>();
    }
    
    @RequestMapping("/posts")
    public List<Post> findPostBy(
        @RequestParam(value = "postType") String postTypeString) {
        
        PostType postType;
        try {
            postType = PostType.valueOf(postTypeString);
        } catch (IllegalArgumentException e) {
            return new ArrayList<Post>();    
        }
        
        // TODO
        // - call PostRepository.findPostBy(postType)
        
        return new ArrayList<Post>();
    }
    
}