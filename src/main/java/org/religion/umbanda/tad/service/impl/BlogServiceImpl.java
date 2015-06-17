package org.religion.umbanda.tad.service.impl;

import org.religion.umbanda.tad.model.Archive;
import org.religion.umbanda.tad.model.Post;
import org.religion.umbanda.tad.model.UserCredentials;
import org.religion.umbanda.tad.model.UserRole;
import org.religion.umbanda.tad.model.VisibilityType;
import org.religion.umbanda.tad.service.BlogService;
import org.religion.umbanda.tad.service.PostRepository;
import org.religion.umbanda.tad.service.UserCredentialsRepository;
import org.religion.umbanda.tad.service.vo.PostResponse;
import org.religion.umbanda.tad.service.vo.UserCredentialsVO;
import org.religion.umbanda.tad.util.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class BlogServiceImpl implements BlogService {

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    @Autowired
    private PostRepository postRepository;

    private VisibilityType doConvertVisibilityType(String visibility) {
        try {
            return VisibilityType.valueOf(visibility);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @RequestMapping("/posts/{userId}")
    @Override
    public List<PostResponse> getPostsByUserId(
        @PathVariable("userId") String userIdAsString) {

        UUID userId;
        try {
            userId = UUID.fromString(userIdAsString);
        } catch (IllegalArgumentException ex) {
            return new ArrayList<PostResponse>();
        }

        final UserCredentials userCredentials = userCredentialsRepository.findById(userId);

        if (userCredentials == null || userCredentials.getUserRole() != UserRole.ADMINISTRATOR) {
            return new ArrayList<PostResponse>();
        }

        return doConvertPost(postRepository.findAll());
    }

    @RequestMapping("/post/archives/{visibility}")
    @Override
    public List<Archive> getArchives(
        @PathVariable("visibility") String visibility) {
        final VisibilityType visibilityType = doConvertVisibilityType(visibility);
        if (visibilityType == null) {
            return new ArrayList<Archive>();
        }
        return postRepository.findArchiveBy(visibilityType);
    }

    @RequestMapping("/published/posts/{visibility}/archive/{year}/{month}")
    @Override
    public List<PostResponse> findPostByArchive(
        @PathVariable("visibility") String visibility,
        @PathVariable("year") int year,
        @PathVariable("month") int month) {
        final VisibilityType visibilityType = doConvertVisibilityType(visibility);
        if (visibilityType == null) {
            return new ArrayList<PostResponse>();
        }
        return doConvertPost(postRepository.findPublishedPost(visibilityType, year, month));
    }
    
    @RequestMapping("/published/posts/{visibility}")
    @Override
    public List<PostResponse> findPublishedPostByVisibility(
        @PathVariable("visibility") String visibility) {
        final VisibilityType visibilityType = doConvertVisibilityType(visibility);
        if (visibilityType == null) {
            return new ArrayList<PostResponse>();
        }
        return doConvertPost(postRepository.findPublishedPost(visibilityType));
    }

    @RequestMapping(value = "/post/{id}", method = RequestMethod.DELETE)
    @Override
    public void removePost(
        @PathVariable("id") String id) {

        UUID postId;
        try {
            postId = UUID.fromString(id);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Id do post é inválido", ex);
        }

        postRepository.removePostById(postId);
    }

    private List<PostResponse> doConvertPost(List<Post> posts) {
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

            final UserCredentials publishedBy = post.getPublishedBy();
            if (publishedBy != null) {
                final UserCredentialsVO publishedByVO = new UserCredentialsVO();
                publishedByVO.setId(publishedBy.getId().toString());
                publishedByVO.setName(publishedBy.getPerson().getName());
                publishedByVO.setUserName(publishedBy.getUserName());
                publishedByVO.setUserRole(publishedBy.getUserRole());
                postResponse.setPublishedBy(publishedByVO);
                postResponse.setPublished(DateTimeUtils.toString(post.getPublished()));
            }

            result.add(postResponse);
        }
        return result;
    }

}