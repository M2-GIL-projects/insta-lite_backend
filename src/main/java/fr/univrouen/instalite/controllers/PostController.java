package fr.univrouen.instalite.controllers;

import fr.univrouen.instalite.entities.Post;
import fr.univrouen.instalite.exceptions.ResourceNotFoundException;
import fr.univrouen.instalite.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        Post createdPost = postService.createPost(post);
        // On diffuse le post créé à tous les abonnés
        messagingTemplate.convertAndSend("/topic/posts", createdPost);
        return ResponseEntity.ok(createdPost);
    }

//    @GetMapping
//    public ResponseEntity<List<Post>> getAllPosts() {
//        return ResponseEntity.ok(postService.getAllPosts());
//    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Post>> getPostsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(postService.getPostsByUser(userId));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable Long postId) {
        Optional<Post> post = postService.getPostById(postId);
        return post.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Le post avec l'ID " + postId + " n'a pas été trouvé."));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        boolean deleted = postService.deletePost(postId);
        if (deleted) {
            // On diffuse une notification de suppression de post
            messagingTemplate.convertAndSend("/topic/posts", "Post supprimé : " + postId);
            return ResponseEntity.ok("Post supprimé avec succès");
        } else {
            throw new ResourceNotFoundException("Le post avec ID " + postId + " n'est pas trouvé.");
        }
    }

    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable Long postId, @RequestBody Post updatedPost) {
        Optional<Post> updated = postService.updatePost(postId, updatedPost);
        if (updated.isPresent()) {
            // Diffusez le post mis à jour à tous les abonnés
            messagingTemplate.convertAndSend("/topic/posts", updated.get());
            return ResponseEntity.ok(updated.get());
        } else {
            throw new ResourceNotFoundException("Le post avec l'ID " + postId + " n'a pas été trouvé.");
        }
    }
}
