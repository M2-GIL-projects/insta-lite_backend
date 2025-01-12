package fr.univrouen.instalite.services;

import fr.univrouen.instalite.entities.Comment;
import fr.univrouen.instalite.entities.Post;
import fr.univrouen.instalite.entities.User;
import fr.univrouen.instalite.exceptions.ResourceNotFoundException;
import fr.univrouen.instalite.repositories.CommentRepository;
import fr.univrouen.instalite.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;


    public Comment addComment(Long postId, String content) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Vous devez être connecté pour créer un commentaire.");
        }
        User currentUser = (User) authentication.getPrincipal();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post introuvable avec l'ID : " + postId));

        Comment comment = new Comment(currentUser, post, content);
        return commentRepository.save(comment);
    }


    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }


    public void deleteComment(Long commentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Vous devez être connecté pour supprimer un commentaire.");
        }else{
            if(commentRepository.existsById(commentId)){
                commentRepository.deleteById(commentId);
            }
        }
    }


    public Comment updateComment(Long commentId, String newContent) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Vous devez être connecté pour modifier un commentaire.");
        }else{
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Commentaire introuvable avec l'ID : " + commentId));
            comment.setContent(newContent);
            User currentUser = (User) authentication.getPrincipal();
            if(Objects.equals(currentUser.getEmail(), comment.getUser().getEmail())){
                return commentRepository.save(comment);
            }else {
                throw new SecurityException("Vous n'avez pas le droit de réaliser cette action.");
            }
        }
    }
}
