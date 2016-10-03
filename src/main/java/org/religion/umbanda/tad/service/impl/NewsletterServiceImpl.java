package org.religion.umbanda.tad.service.impl;

import org.religion.umbanda.tad.model.*;
import org.religion.umbanda.tad.service.MailService;
import org.religion.umbanda.tad.service.NewsletterService;
import org.religion.umbanda.tad.service.NewsletterUserRepository;
import org.religion.umbanda.tad.service.PostRepository;
import org.religion.umbanda.tad.service.vo.NewsletterUserVO;
import org.religion.umbanda.tad.validator.MailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class NewsletterServiceImpl implements NewsletterService {

    @Autowired
    private NewsletterUserRepository newsletterUserRepository;

    @Autowired
    private MailValidator mailValidator;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private MailTemplateFactory mailTemplateFactory;

    @RequestMapping(value = "/newsletters", method = RequestMethod.POST)
    @Override
    public void saveNewsletterUser(@RequestBody NewsletterUserVO newsletterUserVO) {
        if (newsletterUserVO == null) {
            throw new IllegalArgumentException("Não foi possível registrar um usuário. Nenhum dado foi enviado!");
        }

        final String name = newsletterUserVO.getName();
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Por favor, informe o seu nome");
        }

        final String email = newsletterUserVO.getEmail();
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Por favor, informe o seu email");
        }

        if (!mailValidator.validate(email)) {
            throw new IllegalArgumentException("Por favor, informe um e-mail válido");
        }

        NewsletterUser newsletterUser = newsletterUserRepository.findById(newsletterUserVO.getId());
        if (newsletterUser == null) {
            newsletterUser = new NewsletterUser();
        }
        if (newsletterUser.getEmail() == null || !newsletterUser.getEmail().equals(email)) {
            if (newsletterUserRepository.existsByEmail(email)) {
                throw new IllegalStateException(String.format("e-Mail já cadastrado [%s]", email));
            }
        }
        newsletterUser.setName(name);
        newsletterUser.setEmail(email);
        newsletterUserRepository.save(newsletterUser);
    }

    @RequestMapping(value = "/newsletters", method = RequestMethod.GET, produces = "application/json")
    @Override
    public List<NewsletterUserVO> findAll() {
        return newsletterUserRepository.findAll().stream().map(u -> {
            final NewsletterUserVO newsletterUserVO = new NewsletterUserVO();
            newsletterUserVO.setId(u.getId().toString());
            newsletterUserVO.setName(u.getName());
            newsletterUserVO.setEmail(u.getEmail());
            return newsletterUserVO;
        }).collect(Collectors.toList());
    }

    @RequestMapping(value = "/newsletter/{id}", method = RequestMethod.DELETE)
    @Override
    public void removeNewsletterById(@PathVariable("id") String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Id is required");
        }
        newsletterUserRepository.removeById(id);
    }

    @RequestMapping(value = "/newsletter/notify/post/{postId}", method = RequestMethod.POST)
    @Override
    public void notifyUsersByPostPublished(@PathVariable("postId") String postIdString) {
        UUID postId;
        try {
            postId = UUID.fromString(postIdString);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid post id", e);
        }
        final Post post = postRepository.findById(postId);
        if (post != null) {
            final MailTemplate<NewsletterContent> mailTemplate = mailTemplateFactory.getTemplate(NotifyNewsletterUsersPostPublishedMailTemplateConfiguration.KEY);
            for (NewsletterUser newsletterUser : newsletterUserRepository.findAll()) {
                final MailMessage mailMessage = mailTemplate.createMailMessage(new NotifyNewsletterUsersPostPublishedContent(newsletterUser, post));
                mailService.send(mailMessage);
            }
        }
    }
}