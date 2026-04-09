package com.sicklesscript.blog.service;

import com.sicklesscript.blog.model.BlogPost;
import com.sicklesscript.blog.repository.BlogPostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BlogPostService {
    private final BlogPostRepository blogPostRepository;

    public BlogPostService(BlogPostRepository blogPostRepository) {
        this.blogPostRepository = blogPostRepository;
    }

    public List<BlogPost> findAll() {
        return blogPostRepository.findAll()
                .stream()
                .sorted((a, b) -> b.getUpdatedAt().compareTo(a.getUpdatedAt()))
                .toList();
    }

    public Optional<BlogPost> findById(Long id) {
        return blogPostRepository.findById(id);
    }

    public BlogPost create(BlogPost post) {
        return blogPostRepository.save(post);
    }

    @Transactional
    public Optional<BlogPost> update(Long id, BlogPost incoming) {
        return blogPostRepository.findById(id).map(existing -> {
            existing.setTitle(incoming.getTitle());
            existing.setContent(incoming.getContent());
            return existing;
        });
    }

    public boolean delete(Long id) {
        if (!blogPostRepository.existsById(id)) {
            return false;
        }
        blogPostRepository.deleteById(id);
        return true;
    }

    public void seedIfEmpty() {
        if (blogPostRepository.count() > 0) {
            return;
        }

        BlogPost welcome = new BlogPost();
        welcome.setTitle("Welcome to SicklesScript");
        welcome.setContent("This is your first post. Edit or delete it, then create your own.");
        create(welcome);

        BlogPost stylePost = new BlogPost();
        stylePost.setTitle("Terminal Vibes");
        stylePost.setContent("Green-on-dark, simple controls, and fast CRUD to get started.");
        create(stylePost);
    }
}
