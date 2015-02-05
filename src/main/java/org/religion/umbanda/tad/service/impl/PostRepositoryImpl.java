package org.religion.umbanda.tad.service.impl;

import org.apache.catalina.User;
import org.joda.time.DateTime;
import org.religion.umbanda.tad.model.*;
import org.religion.umbanda.tad.service.PostResistory;
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
public class PostRepositoryImpl implements PostResistory {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PostRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Post> findPublishedPost(VisibilityType visibilityType) {
        return jdbcTemplate.query(
            "select * from Post where visibility_type = ? and published_date is not null order by published_date",
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
                    post.setCreated(new DateTime(rs.getLong("created")));
                    final UserCredentials createdBy = new UserCredentials();
                    createdBy.setId(UUID.fromString(rs.getString("createdBy")));
                    post.setCreatedBy(createdBy);
                    post.setModified(new DateTime(rs.getLong("modified")));
                    final UserCredentials modifiedBy = new UserCredentials();
                    modifiedBy.setId(UUID.fromString(rs.getString("modifiedBy")));
                    post.setModifiedBy(modifiedBy);
                    post.setPublished(new DateTime(rs.getLong("published")));
                    final UserCredentials publishedBy = new UserCredentials();
                    publishedBy.setId(UUID.fromString(rs.getString("publishedBy")));
                    post.setPublishedBy(publishedBy);
                    return post;
                }
            }
        );
    }

    @Transactional(readOnly = true)
    @Override
    public List<Archive> getArchives() {
        return jdbcTemplate.query("select published_date from Post where published_date is not null order by published_date", new RowMapper<Archive>() {
            @Override
            public Archive mapRow(ResultSet rs, int i) throws SQLException {
                final Archive archive = new Archive();
                archive.setArchived(new DateTime(rs.getLong("published_date")));
                return archive;
            }
        });
    }
}
