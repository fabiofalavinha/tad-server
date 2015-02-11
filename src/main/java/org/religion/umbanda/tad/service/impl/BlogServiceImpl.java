package org.religion.umbanda.tad.service.impl;

import java.util.List;
import java.util.ArrayList;

import org.religion.umbanda.tad.model.Archive;
import org.religion.umbanda.tad.model.Post;
import org.religion.umbanda.tad.model.VisibilityType;
import org.religion.umbanda.tad.service.BlogService;

import org.religion.umbanda.tad.service.PostRepository;
import org.religion.umbanda.tad.service.vo.PostResponse;
import org.religion.umbanda.tad.service.vo.UserCredentialsVO;
import org.religion.umbanda.tad.util.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogServiceImpl implements BlogService {

    @Autowired
    private PostRepository postRepository;

    @RequestMapping("/post/archives")
    @Override
    public List<Archive> getArchives() {
        return postRepository.getArchives();
    }
    
    @RequestMapping("/published/posts/{visibility}")
    @Override
    public List<PostResponse> findPublishedPostByVisibility(
        @PathVariable("visibility") String visibility) {
        VisibilityType visibilityType;
        try {
            visibilityType = VisibilityType.valueOf(visibility);
        } catch (IllegalArgumentException e) {
            return new ArrayList<PostResponse>();
        }
        final List<Post> posts = postRepository.findPublishedPost(visibilityType);
        final List<PostResponse> result = new ArrayList<PostResponse>(posts.size());
        for (Post post : posts) {
            final PostResponse postResponse = new PostResponse();
            postResponse.setId(post.getId().toString());
            postResponse.setTitle(post.getTitle());
            postResponse.setContent(post.getContent());
            postResponse.setPostType(post.getPostType().name());
            postResponse.setVisibilityType(post.getVisibilityType().name());
            final UserCredentialsVO createdBy = new UserCredentialsVO();
            createdBy.setId(post.getCreatedBy().getId().toString());
            createdBy.setName(post.getCreatedBy().getPerson().getName());
            createdBy.setUserName(post.getCreatedBy().getUserName());
            createdBy.setUserRole(post.getCreatedBy().getUserRole());
            postResponse.setCreatedBy(createdBy);
            postResponse.setCreated(DateTimeUtils.toString(post.getCreated()));
            final UserCredentialsVO modifiedBy = new UserCredentialsVO();
            modifiedBy.setId(post.getModifiedBy().getId().toString());
            modifiedBy.setName(post.getModifiedBy().getPerson().getName());
            modifiedBy.setUserName(post.getModifiedBy().getUserName());
            modifiedBy.setUserRole(post.getModifiedBy().getUserRole());
            postResponse.setModifiedBy(modifiedBy);
            postResponse.setModified(DateTimeUtils.toString(post.getModified()));
            final UserCredentialsVO publishedBy = new UserCredentialsVO();
            publishedBy.setId(post.getPublishedBy().getId().toString());
            publishedBy.setName(post.getPublishedBy().getPerson().getName());
            publishedBy.setUserName(post.getPublishedBy().getUserName());
            publishedBy.setUserRole(post.getPublishedBy().getUserRole());
            postResponse.setPublishedBy(publishedBy);
            postResponse.setPublished(DateTimeUtils.toString(post.getPublished()));
            result.add(postResponse);
        }
        return result;
    }
    
}