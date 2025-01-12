package fr.univrouen.instalite.controllers;

import fr.univrouen.instalite.entities.Like;
import fr.univrouen.instalite.services.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    // On ajoute un like
    @PostMapping("/{postId}")
    public ResponseEntity<Like> addLike(@PathVariable Long postId) {
        Like like = likeService.addLike(postId);
        return ResponseEntity.ok(like);
    }

    // On récupére tous les likes d'un post
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Like>> getLikesByPostId(@PathVariable Long postId) {
        List<Like> likes = likeService.getLikesByPostId(postId);
        return ResponseEntity.ok(likes);
    }

    // On supprime un like
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> removeLike(@PathVariable Long postId) {
        likeService.removeLike(postId);
        return ResponseEntity.ok("Like supprimé avec succès.");
    }
}
