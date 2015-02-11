package org.religion.umbanda.tad.service.impl;

import org.joda.time.DateTime;
import org.religion.umbanda.tad.model.*;
import org.religion.umbanda.tad.service.PostRepository;
import org.religion.umbanda.tad.service.UserCredentialsRepository;
import org.religion.umbanda.tad.util.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final JdbcTemplate jdbcTemplate;
    private final UserCredentialsRepository userCredentialsRepository;

    @Autowired
    public PostRepositoryImpl(JdbcTemplate jdbcTemplate, UserCredentialsRepository userCredentialsRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.userCredentialsRepository = userCredentialsRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Post> findPublishedPost(VisibilityType visibilityType) {
        final List<Post> posts = jdbcTemplate.query(
            "select * from Post where visibility_type = ? and published is not null order by published",
            new Object[] { visibilityType.getValue() },
            new RowMapper<Post>() {
                @Override
                public Post mapRow(ResultSet rs, int i) throws SQLException {
                    final Post post = new Post();
                    post.setId(UUID.fromString(rs.getString("id")));
                    post.setTitle(rs.getString("title"));
                    post.setContent(rs.getString("content"));
                    post.setVisibilityType(VisibilityType.fromValue(rs.getInt("visibility_type")));
                    post.setPostType(PostType.fromValue(rs.getInt("post_type")));

                    final UserCredentials createdBy = new UserCredentials();
                    createdBy.setId(UUID.fromString(rs.getString("created_by")));
                    post.setCreatedBy(createdBy);
                    post.setCreated(DateTimeUtils.fromString(rs.getString("created")));

                    final UserCredentials modifiedBy = new UserCredentials();
                    modifiedBy.setId(UUID.fromString(rs.getString("modified_by")));
                    post.setModifiedBy(modifiedBy);
                    post.setModified(DateTimeUtils.fromString(rs.getString("modified")));

                    final UserCredentials publishedBy = new UserCredentials();
                    publishedBy.setId(UUID.fromString(rs.getString("published_by")));
                    post.setPublishedBy(publishedBy);
                    post.setPublished(DateTimeUtils.fromString(rs.getString("published")));

                    return post;
                }
            }
        );
        for (Post post : posts) {
            UserCredentials createdBy = post.getCreatedBy();
            createdBy = userCredentialsRepository.findById(createdBy.getId());
            post.setCreatedBy(createdBy);

            UserCredentials modifiedBy = post.getModifiedBy();
            modifiedBy = userCredentialsRepository.findById(modifiedBy.getId());
            post.setModifiedBy(modifiedBy);

            UserCredentials publishedBy = post.getPublishedBy();
            publishedBy = userCredentialsRepository.findById(publishedBy.getId());
            post.setPublishedBy(publishedBy);
        }
        return posts;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Archive> getArchives() {
        return jdbcTemplate.query(
            "select published, count(*) as quantity from Post where published is not null group by strftime('%m-%Y', published) order by published",
            new RowMapper<Archive>() {
                @Override
                public Archive mapRow(ResultSet rs, int i) throws SQLException {
                    final Archive archive = new Archive();
                    archive.setArchived(DateTimeUtils.fromString(rs.getString("published")));
                    archive.setCount(rs.getInt("quantity"));
                    return archive;
                }
            }
        );
    }
}
