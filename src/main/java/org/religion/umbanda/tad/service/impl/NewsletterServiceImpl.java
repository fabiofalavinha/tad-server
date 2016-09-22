package org.religion.umbanda.tad.service.impl;

import org.religion.umbanda.tad.model.NewsletterUser;
import org.religion.umbanda.tad.service.NewsletterUserRepository;
import org.religion.umbanda.tad.service.NewsletterService;
import org.religion.umbanda.tad.service.vo.NewsletterUserVO;
import org.religion.umbanda.tad.validator.MailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class NewsletterServiceImpl implements NewsletterService {

    @Autowired
    private NewsletterUserRepository newsletterUserRepository;

    @RequestMapping(value = "/newsletters", method = RequestMethod.POST)
    @Override
    public void saveNewsletterUser(@RequestBody NewsletterUserVO newsletterUserVO) {
        String email = newsletterUserVO.getEmail();
        if (!newsletterUserRepository.existsByEmail(email)) {
            final NewsletterUser newsletterUser = new NewsletterUser();
            String name = newsletterUserVO.getName();
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("Por favor, informe o seu nome");
            }
            newsletterUser.setName(name);

            if (email == null || email.isEmpty()) {
                throw new IllegalArgumentException("Por favor, informe o seu email");
            }

            MailValidator mailValidator = new MailValidator();
            if (!mailValidator.validate(email)) {
                throw new IllegalArgumentException("Por favor, informe um e-mail válido");
            }
            newsletterUser.setEmail(email);

            newsletterUserRepository.add(newsletterUser);
        }
    }

    @RequestMapping(value = "/newsletters", method = RequestMethod.GET, produces = "application/json")
    @Override
    public List<NewsletterUserVO> findAll() {
        return newsletterUserRepository.findAll().stream().map(u -> {
            NewsletterUserVO newsletterUserVO = new NewsletterUserVO();
            newsletterUserVO.setName(u.getName());
            newsletterUserVO.setEmail(u.getEmail());
            return newsletterUserVO;
        }).collect(Collectors.toList());
    }
}