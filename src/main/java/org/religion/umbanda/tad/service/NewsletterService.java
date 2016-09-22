package org.religion.umbanda.tad.service;

import org.religion.umbanda.tad.service.vo.NewsletterUserVO;
import java.util.List;

public interface NewsletterService {

    List<NewsletterUserVO> findAll();
    void saveNewsletterUser(NewsletterUserVO newsletterUserVO);
}