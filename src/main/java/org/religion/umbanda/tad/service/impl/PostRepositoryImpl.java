package org.religion.umbanda.tad.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;
import org.religion.umbanda.tad.model.*;
import org.religion.umbanda.tad.service.PostRepository;
import org.religion.umbanda.tad.service.UserCredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private static final int MAX_PAGE_ITEMS_COUNT = 5;

    private final JdbcTemplate jdbcTemplate;
    private final UserCredentialsRepository userCredentialsRepository;
    private final RowMapper<Post> postRowMapper = new RowMapper<Post>() {
        @Override
        public Post mapRow(ResultSet rs, int i) throws SQLException {
            final Post post = new Post();
            post.setId(UUID.fromString(rs.getString("id")));
            post.setTitle(rs.getString("title"));
            post.setContent(rs.getString("content"));
            post.setVisibilityType(VisibilityType.fromValue(rs.getInt("visibility_type")));
            post.setPostType(PostType.fromValue(rs.getInt("post_type")));
            post.setOrder(rs.getInt("order"));

            final UserCredentials createdBy = new UserCredentials();
            createdBy.setId(UUID.fromString(rs.getString("created_by")));
            post.setCreatedBy(createdBy);
            post.setCreated(new DateTime(rs.getLong("created")));

            final UserCredentials modifiedBy = new UserCredentials();
            modifiedBy.setId(UUID.fromString(rs.getString("modified_by")));
            post.setModifiedBy(modifiedBy);
            post.setModified(new DateTime(rs.getLong("modified")));

            final String publishedById = rs.getString("published_by");
            if (publishedById != null && !"".equals(publishedById)) {
                final UserCredentials publishedBy = new UserCredentials();
                publishedBy.setId(UUID.fromString(publishedById));
                post.setPublishedBy(publishedBy);
                post.setPublished(new DateTime(rs.getLong("published")));
            }

            return post;
        }
    };

    @Autowired
    public PostRepositoryImpl(JdbcTemplate jdbcTemplate, UserCredentialsRepository userCredentialsRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.userCredentialsRepository = userCredentialsRepository;
    }

    private void doSetAuditProperties(List<Post> posts) {
        for (Post post : posts) {
            UserCredentials createdBy = post.getCreatedBy();
            createdBy = userCredentialsRepository.findById(createdBy.getId());
            post.setCreatedBy(createdBy);

            UserCredentials modifiedBy = post.getModifiedBy();
            modifiedBy = userCredentialsRepository.findById(modifiedBy.getId());
            post.setModifiedBy(modifiedBy);

            UserCredentials publishedBy = post.getPublishedBy();
            if (publishedBy != null) {
                publishedBy = userCredentialsRepository.findById(publishedBy.getId());
                post.setPublishedBy(publishedBy);
            }
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<Post> findAll() {
        return doFindPost("select * from Post order by [order]");
    }

    @Transactional(readOnly = true)
    @Override
    public PostPageable findPublishedPost(VisibilityType visibilityType, int pageNumber) {
        int previousPage = 0;
        if (pageNumber > 1) {
            previousPage = pageNumber - 1;
        }

        final int start = previousPage * MAX_PAGE_ITEMS_COUNT;
        final int count = jdbcTemplate.queryForObject("select count(*) from Post where visibility_type = ? and published > 0 order by [order]", Integer.class, visibilityType.getValue());

        final String queryTemplate = "select * from Post where visibility_type = ? and published > 0 order by [order] limit %d, %d";
        final String query = String.format(queryTemplate, start, MAX_PAGE_ITEMS_COUNT);

        final List<Post> posts = doFindPost(query, visibilityType.getValue());

        final PostPageable postPageable = new PostPageable();
        postPageable.setPosts(posts);
        postPageable.setCount(count);
        postPageable.setPageCount(MAX_PAGE_ITEMS_COUNT);
        postPageable.setHasNext(start + MAX_PAGE_ITEMS_COUNT < count);
        postPageable.setHasPrevious(start > 0);
        postPageable.setPageNumber(pageNumber);

        return postPageable;
    }

    @Transactional(readOnly = true)
    @Override
    public PostPageable findPublishedPost(VisibilityType visibilityType, int year, int month, int pageNumber) {
        final List<Post> posts =
            doFindPost("select * from Post where visibility_type = ? and published > 0 order by [order]", visibilityType.getValue())
                .stream().filter(p -> p.getPublished().getYear() == year && p.getPublished().getMonthOfYear() == month).collect(Collectors.toList());

        final int count = posts.size();
        int previousPage = 0;
        if (pageNumber > 1) {
            previousPage = pageNumber - 1;
        }

        final int start = previousPage * MAX_PAGE_ITEMS_COUNT;
        int end = start + MAX_PAGE_ITEMS_COUNT;
        if (end > posts.size()) {
            end = posts.size();
        }
        final List<Post> postsSubList = posts.subList(start, end);

        final PostPageable postPageable = new PostPageable();
        postPageable.setPosts(postsSubList);
        postPageable.setCount(count);
        postPageable.setPageCount(MAX_PAGE_ITEMS_COUNT);
        postPageable.setHasNext(start + MAX_PAGE_ITEMS_COUNT < count);
        postPageable.setHasPrevious(start > 0);
        postPageable.setPageNumber(pageNumber);

        return postPageable;
    }

    private List<Post> doFindPost(String queryString, Object... parameters) {
        final List<Post> posts = jdbcTemplate.query(queryString, parameters, postRowMapper);
        doSetAuditProperties(posts);
        return posts;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Archive> findArchiveBy(VisibilityType visibilityType) {
        final List<Archive> archives = new ArrayList<>();
        jdbcTemplate.query(
                "select published from Post where published > 0 and visibility_type = ? order by [order]",
                new Object[]{visibilityType.getValue()},
                resultSet -> {
                    final DateTime published = new DateTime(resultSet.getLong("published"));
                    Archive found = (Archive) CollectionUtils.find(archives, new Predicate() {
                        @Override
                        public boolean evaluate(Object o) {
                            final Archive archive = (Archive) o;
                            return archive.getArchived().getMonthOfYear() == published.getMonthOfYear() &&
                                    archive.getArchived().getYear() == published.getYear();
                        }
                    });
                    if (found == null) {
                        found = new Archive();
                        found.setArchived(published);
                        archives.add(found);
                    }
                    found.increaseCount();
                }
        );
        return archives;
    }

    @Transactional(readOnly = true)
    @Override
    public Post findById(UUID postId) {
        final List<Post> posts = doFindPost("select * from Post where id = ?", postId.toString());
        if (posts.isEmpty()) {
            return null;
        }
        return posts.get(0);
    }

    @Transactional
    @Override
    public void removePostById(UUID postId) {
        jdbcTemplate.update("delete from Post where id = ?", postId.toString());
    }

    @Transactional
    @Override
    public void createPost(Post post) {
        String publishedById = "";
        final UserCredentials publishedBy = post.getPublishedBy();
        if (publishedBy != null) {
            publishedById = publishedBy.getId().toString();
        }
        long publishedMillis = 0;
        DateTime published = post.getPublished();
        if (published != null) {
            publishedMillis = post.getPublished().getMillis();
        }
        jdbcTemplate.update(
                "insert into Post (id, title, content, visibility_type, post_type, created_by, created, modified_by, modified, published_by, published, [order]) " +
                        "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                post.getId().toString(), post.getTitle(), post.getContent(), post.getVisibilityType().getValue(), post.getPostType().getValue(),
                post.getCreatedBy().getId().toString(), post.getCreated().getMillis(), post.getModifiedBy().getId().toString(), post.getModified().getMillis(),
                publishedById, publishedMillis, post.getOrder()
        );
    }

    @Transactional
    @Override
    public void updatePost(Post post) {
        String publishedById = "";
        final UserCredentials publishedBy = post.getPublishedBy();
        if (publishedBy != null) {
            publishedById = publishedBy.getId().toString();
        }
        long publishedMillis = 0;
        DateTime published = post.getPublished();
        if (published != null) {
            publishedMillis = post.getPublished().getMillis();
        }
        jdbcTemplate.update(
                "update Post set title=?, content=?, visibility_type=?, post_type=?, created_by=?, created=?, modified_by=?, modified=?, published_by=?, published=?, [order]=? where id=?",
                post.getTitle(), post.getContent(), post.getVisibilityType().getValue(), post.getPostType().getValue(),
                post.getCreatedBy().getId().toString(), post.getCreated().getMillis(), post.getModifiedBy().getId().toString(),
                post.getModified().getMillis(), publishedById, publishedMillis, post.getOrder(), post.getId().toString()
        );
    }
}
