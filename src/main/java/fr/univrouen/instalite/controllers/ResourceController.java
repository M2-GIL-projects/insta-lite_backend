package fr.univrouen.instalite.controllers;


import fr.univrouen.instalite.entities.Post;
import fr.univrouen.instalite.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class ResourceController {
    @Autowired
    private PostService postService;

    @GetMapping("/")
    public ResponseEntity<?> getResource() {
    List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/public")
    public ResponseEntity<?> getPublicResource() {
        List<Post> posts = postService.getPublicPosts();
        return ResponseEntity.ok(posts);
    }

}
