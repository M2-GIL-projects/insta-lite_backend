package fr.univrouen.instalite.controllers;


import fr.univrouen.instalite.entities.Post;
import fr.univrouen.instalite.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class PortfolioController {
    @Autowired
    private PostService postService;

    @GetMapping("/")
    public ResponseEntity<?> getPublicPortfolio() {
        List<Post> posts = postService.getPostsByRole();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/portfolio/public")
    public ResponseEntity<?> getPortfolio() {
        List<Post> posts = postService.getPublicPosts();
        return ResponseEntity.ok(posts);
    }

    @PreAuthorize("hasRole('PRIVILEGED_USER')")
    @GetMapping("/portfolio/private")
    public ResponseEntity<?> getAllPortfolio() {
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }



}
