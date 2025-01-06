package fr.univrouen.instalite.controllers;

import fr.univrouen.instalite.entities.Post;
import fr.univrouen.instalite.entities.User;
import fr.univrouen.instalite.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostRepository postRepository;


    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody Post post) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(403).body("Vous devez être connecté pour créer un post.");
        }
        User currentUser = (User) authentication.getPrincipal();
        post.setUser(currentUser);
        Post savedPost = postRepository.save(post);

        return ResponseEntity.ok(savedPost);
    }


    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        return ResponseEntity.ok(postRepository.findAll());
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Post>> getPostsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(postRepository.findByUserId(userId));
    }


    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            return ResponseEntity.ok(postOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        if (postRepository.existsById(postId)) {
            postRepository.deleteById(postId);
            return ResponseEntity.ok("Post supprimé avec succès");
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable Long postId, @RequestBody Post updatedPost) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post existingPost = postOptional.get();
            existingPost.setContent(updatedPost.getContent());
            existingPost.setPrivate(updatedPost.isPrivate());
            Post savedPost = postRepository.save(existingPost);
            return ResponseEntity.ok(savedPost);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
