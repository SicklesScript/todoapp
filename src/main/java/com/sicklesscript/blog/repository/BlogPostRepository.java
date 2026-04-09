package com.sicklesscript.blog.repository;

import com.sicklesscript.blog.model.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
}
