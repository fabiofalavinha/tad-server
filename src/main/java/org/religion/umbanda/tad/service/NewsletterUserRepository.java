package org.religion.umbanda.tad.service;

import org.religion.umbanda.tad.model.NewsletterUser;

import java.util.List;

public interface NewsletterUserRepository {

    List<NewsletterUser> findAll();

    boolean existsByEmail(String email);

    NewsletterUser findById(String id);

    void save(NewsletterUser newsletterUser);

    void removeById(String id);

}