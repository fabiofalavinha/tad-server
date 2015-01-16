package org.religion.umbanda.tad.model;

import java.util.UUID;
import org.joda.time.DateTime;
import org.religion.umbanda.tad.model.PostType;
import org.religion.umbanda.tad.model.VisibilityType;
import org.religion.umbanda.tad.model.UserCredentials;

public class Post {
    
    private UUID id;
    private String content;
    private PostType postType;
    private VisibilityType visibilityType;
    private UserCredentials createdBy;
    private DateTime created;
    private UserCredentials modifiedBy;
    private DateTime modified;
    private UserCredentials publishedBy;
    private DateTime published;
    
}