package org.religion.umbanda.tad.service.impl;

import org.joda.time.DateTime;
import org.religion.umbanda.tad.model.*;
import org.religion.umbanda.tad.service.BlogService;
import org.religion.umbanda.tad.service.PostRepository;
import org.religion.umbanda.tad.service.UserCredentialsRepository;
import org.religion.umbanda.tad.service.vo.PostPageableDTO;
import org.religion.umbanda.tad.service.vo.PostRequest;
import org.religion.umbanda.tad.service.vo.PostResponse;
import org.religion.umbanda.tad.service.vo.UserCredentialsVO;
import org.religion.umbanda.tad.util.DateTimeUtils;
import org.religion.umbanda.tad.util.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping("/post/{postId}")
    @Override
    public PostResponse getPostById(
            @PathVariable("postId") String postIdAsString) {
        try {
            final UUID id = IdUtils.fromString(postIdAsString);
            return doConvertPost(postRepository.findById(id));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @RequestMapping("/posts/{userId}")
    @Override
    public List<PostResponse> getPostsByUserId(
            @PathVariable("userId") String userIdAsString) {
        final UUID userId = IdUtils.fromString(userIdAsString);
        final UserCredentials userCredentials = userCredentialsRepository.findById(userId);
        if (userCredentials == null || userCredentials.getUserRole() != UserRole.ADMINISTRATOR) {
            return new ArrayList<>();
        }
        return doConvertPostList(postRepository.findAll());
    }

    @RequestMapping("/post/archives/{visibility}")
    @Override
    public List<Archive> getArchives(
            @PathVariable("visibility") String visibility) {
        final VisibilityType visibilityType = doConvertVisibilityType(visibility);
        if (visibilityType == null) {
            return new ArrayList<>();
        }
        return postRepository.findArchiveBy(visibilityType);
    }

    @RequestMapping("/published/posts/{visibility}/archive/{year}/{month}?p={pageNumber}")
    @Override
    public PostPageableDTO findPostByArchive(
            @PathVariable("visibility") String visibility,
            @PathVariable("year") int year,
            @PathVariable("month") int month,
            @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber) {
        final VisibilityType visibilityType = doConvertVisibilityType(visibility);
        if (visibilityType == null) {
            return new PostPageableDTO();
        }

        if (pageNumber <= 0) {
            pageNumber = 1;
        }

        final PostPageable postPageable = postRepository.findPublishedPost(visibilityType, year, month, pageNumber);

        final PostPageableDTO dto = new PostPageableDTO();
        dto.setPosts(doConvertPostList(postPageable.getPosts()));
        dto.setHasNext(postPageable.isHasNext());
        dto.setHasPrevious(postPageable.isHasPrevious());
        dto.setPageCount(postPageable.getPageCount());
        dto.setCount(postPageable.getCount());

        return dto;
    }

    @RequestMapping("/published/posts/{visibility}?p={pageNumber}")
    @Override
    public PostPageableDTO findPublishedPostByVisibility(
            @PathVariable("visibility") String visibility,
            @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber) {
        final VisibilityType visibilityType = doConvertVisibilityType(visibility);
        if (visibilityType == null) {
            return new PostPageableDTO();
        }

        if (pageNumber <= 0) {
            pageNumber = 1;
        }

        final PostPageable postPageable = postRepository.findPublishedPost(visibilityType, pageNumber);

        final PostPageableDTO dto = new PostPageableDTO();
        dto.setPosts(doConvertPostList(postPageable.getPosts()));
        dto.setHasNext(postPageable.isHasNext());
        dto.setHasPrevious(postPageable.isHasPrevious());
        dto.setPageCount(postPageable.getPageCount());
        dto.setCount(postPageable.getCount());

        return dto;
    }

    @RequestMapping(value = "/post/{id}", method = RequestMethod.DELETE)
    @Override
    public void removePost(
            @PathVariable("id") String id) {
        postRepository.removePostById(IdUtils.fromString(id));
    }

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    @Override
    public void savePost(
            @RequestBody PostRequest postRequest) {

        if (postRequest == null) {
            throw new IllegalArgumentException("Dados do post são inválido");
        }

        boolean isNewPost = false;
        Post post;

        final String postIdAsString = postRequest.getId();
        if (postIdAsString == null || "".equals(postIdAsString.trim())) {
            isNewPost = true;
            post = new Post();
            post.setPostType(PostType.GENERAL);
            post.setId(UUID.randomUUID());
        } else {
            final UUID postId = IdUtils.fromString(postIdAsString);
            post = postRepository.findById(postId);
            if (post == null) {
                throw new IllegalStateException(String.format("Não foi possível alterar o post [id=%s]", postId));
            }
            post.setOrder(postRequest.getOrder());
        }


        final String postTitle = postRequest.getTitle();
        if (postTitle == null || "".equals(postTitle)) {
            throw new IllegalStateException("É obrigatório informar o título do post");
        }
        post.setTitle(postTitle);

        final String postContent = postRequest.getContent();
        if (postContent == null || "".equals(postContent)) {
            throw new IllegalStateException("É obrigatório escrever o conteúdo do post");
        }
        post.setContent(postContent);

        post.setCreated(DateTime.parse(postRequest.getCreated()));
        final UserCredentialsVO createdByVO = postRequest.getCreatedBy();
        if (createdByVO == null) {
            throw new IllegalStateException("É obrigatório informar o usuário que criou o post");
        }
        final UUID createdById = IdUtils.fromString(createdByVO.getId());
        final UserCredentials createdBy = userCredentialsRepository.findById(createdById);
        if (createdBy == null) {
            throw new IllegalStateException(String.format("Não foi possível encontrar os dados do usuário que criou o post: %s", createdById.toString()));
        }
        post.setCreatedBy(createdBy);

        post.setModified(DateTime.parse(postRequest.getModified()));
        final UserCredentialsVO modifiedByVO = postRequest.getModifiedBy();
        if (modifiedByVO == null) {
            throw new IllegalStateException("É obrigatório informar o usuário que fez a modificação no post");
        }
        final UUID modifiedById = IdUtils.fromString(modifiedByVO.getId());
        final UserCredentials modifiedBy = userCredentialsRepository.findById(modifiedById);
        if (modifiedBy == null) {
            throw new IllegalStateException(String.format("Não foi possível encontrar os dados do usuário que fez a modificação no post: %s", modifiedById.toString()));
        }
        post.setModifiedBy(modifiedBy);

        DateTime published = null;
        final String postPublished = postRequest.getPublished();
        if (postPublished != null && !"".equals(postPublished.trim())) {
            published = DateTime.parse(postPublished);
        }
        post.setPublished(published);

        UserCredentials publishedBy = null;
        final UserCredentialsVO publishedByVO = postRequest.getPublishedBy();
        if (publishedByVO != null) {
            final UUID publishedById = IdUtils.fromString(publishedByVO.getId());
            publishedBy = userCredentialsRepository.findById(publishedById);
            if (publishedBy == null) {
                throw new IllegalStateException(String.format("Não foi possível encontrar os dados do usuário que publicou o post: %s", publishedById.toString()));
            }
        }
        post.setPublishedBy(publishedBy);

        post.setVisibilityType(postRequest.getVisibility());

        if (isNewPost) {
            postRepository.createPost(post);
        } else {
            postRepository.updatePost(post);
        }
    }

    private List<PostResponse> doConvertPostList(List<Post> posts) {
        final List<PostResponse> result = new ArrayList<>(posts.size());
        for (Post post : posts) {
            result.add(doConvertPost(post));
        }
        return result;
    }

    private PostResponse doConvertPost(Post post) {
        final PostResponse postResponse = new PostResponse();
        postResponse.setId(post.getId().toString());
        postResponse.setTitle(post.getTitle());
        postResponse.setContent(post.getContent());
        postResponse.setPostType(post.getPostType().name());
        postResponse.setVisibility(post.getVisibilityType().name());

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
            postResponse.setPublishedDateFormat(DateTimeUtils.toString(post.getPublished(), "dd/MMMM/yy HH:mm"));
        }
        return postResponse;
    }

}