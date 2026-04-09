package com.sicklesscript.blog.controller;

import com.sicklesscript.blog.model.BlogPost;
import com.sicklesscript.blog.service.BlogPostService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BlogPostController {
    private final BlogPostService blogPostService;

    public BlogPostController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
        this.blogPostService.seedIfEmpty();
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/posts";
    }

    @GetMapping("/posts")
    public String listPosts(Model model) {
        model.addAttribute("posts", blogPostService.findAll());
        return "posts";
    }

    @GetMapping("/posts/new")
    public String newPostForm(Model model) {
        model.addAttribute("post", new BlogPost());
        model.addAttribute("formAction", "/posts");
        model.addAttribute("formTitle", "Create New Post");
        model.addAttribute("submitLabel", "Create");
        return "post-form";
    }

    @PostMapping("/posts")
    public String createPost(@Valid @ModelAttribute("post") BlogPost post,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formAction", "/posts");
            model.addAttribute("formTitle", "Create New Post");
            model.addAttribute("submitLabel", "Create");
            return "post-form";
        }
        blogPostService.create(post);
        redirectAttributes.addFlashAttribute("message", "Post created.");
        return "redirect:/posts";
    }

    @GetMapping("/posts/{id}")
    public String viewPost(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return blogPostService.findById(id)
                .map(post -> {
                    model.addAttribute("post", post);
                    return "post-view";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("message", "Post not found.");
                    return "redirect:/posts";
                });
    }

    @GetMapping("/posts/{id}/edit")
    public String editPostForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return blogPostService.findById(id)
                .map(post -> {
                    model.addAttribute("post", post);
                    model.addAttribute("formAction", "/posts/" + id + "/update");
                    model.addAttribute("formTitle", "Edit Post");
                    model.addAttribute("submitLabel", "Save");
                    return "post-form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("message", "Post not found.");
                    return "redirect:/posts";
                });
    }

    @PostMapping("/posts/{id}/update")
    public String updatePost(@PathVariable Long id,
                             @Valid @ModelAttribute("post") BlogPost post,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formAction", "/posts/" + id + "/update");
            model.addAttribute("formTitle", "Edit Post");
            model.addAttribute("submitLabel", "Save");
            return "post-form";
        }

        boolean updated = blogPostService.update(id, post).isPresent();
        redirectAttributes.addFlashAttribute("message", updated ? "Post updated." : "Post not found.");
        return "redirect:/posts";
    }

    @PostMapping("/posts/{id}/delete")
    public String deletePost(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean deleted = blogPostService.delete(id);
        redirectAttributes.addFlashAttribute("message", deleted ? "Post deleted." : "Post not found.");
        return "redirect:/posts";
    }
}
