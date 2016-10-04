package org.religion.umbanda.tad.service;

import org.religion.umbanda.tad.service.vo.NewsletterUserVO;

import java.util.List;

public interface NewsletterService {

    List<NewsletterUserVO> findAll();

    String confirmUserMail(String id);

    void saveNewsletterUser(NewsletterUserVO newsletterUserVO);

    void removeNewsletterById(String id);

    void notifyUsersByPostPublished(String postIdString);

}