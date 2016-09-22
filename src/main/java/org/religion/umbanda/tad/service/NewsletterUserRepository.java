package org.religion.umbanda.tad.service;

import org.religion.umbanda.tad.model.NewsletterUser;
import java.util.List;

public interface NewsletterUserRepository {

    List<NewsletterUser> findAll();

    boolean existsByEmail(String email);

    void add(NewsletterUser newsletterUser);

}