package fr.univrouen.instalite.controllers;

import fr.univrouen.instalite.entities.Comment;
import fr.univrouen.instalite.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // On ajoute un commentaire
    @PostMapping("/add/{postId}")
    public ResponseEntity<Comment> addComment(@PathVariable Long postId, @RequestBody String content) {
        Comment comment = commentService.addComment(postId, content);
        return ResponseEntity.ok(comment);
    }

    // On récupére tous les commentaires pour un post
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable Long postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    // On supprime un commentaire
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok("Commentaire supprimé avec succès.");
    }

    // On modifie un commentaire
    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long commentId, @RequestBody String newContent) {
        Comment updatedComment = commentService.updateComment(commentId, newContent);
        return ResponseEntity.ok(updatedComment);
    }
}
