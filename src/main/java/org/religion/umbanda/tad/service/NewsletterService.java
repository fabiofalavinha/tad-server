package org.religion.umbanda.tad.service;

import org.religion.umbanda.tad.service.vo.NewsletterUserVO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

public interface NewsletterService {

    List<NewsletterUserVO> findAll();
    void saveNewsletterUser(NewsletterUserVO newsletterUserVO);

    @RequestMapping(value = "/newsletter/{id}", method = RequestMethod.DELETE)
    void removeNewsletterById(@PathVariable("id") String id);

    @RequestMapping(value = "/newsletter/post/{postId}", method = RequestMethod.POST)
    void notifyUsersByPostPublished(@PathVariable("postId") String postIdString);
}