package fr.univrouen.instalite.services;

import fr.univrouen.instalite.entities.Like;
import fr.univrouen.instalite.entities.Post;
import fr.univrouen.instalite.entities.User;
import fr.univrouen.instalite.exceptions.ResourceNotFoundException;
import fr.univrouen.instalite.repositories.LikeRepository;
import fr.univrouen.instalite.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PostRepository postRepository;

    public Like addLike(Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Vous devez être connecté pour liker un post.");
        }
        User currentUser = (User) authentication.getPrincipal();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post introuvable avec l'ID : " + postId));

        Optional<Like> existingLike = likeRepository.findByUserAndPost(currentUser, post);
        if (existingLike.isPresent()) {
            return existingLike.get();
        }
        Like like = new Like(currentUser, post);
        return likeRepository.save(like);
    }

    public List<Like> getLikesByPostId(Long postId) {
        return likeRepository.findByPostId(postId);
    }

    public void removeLike(Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Vous devez être connecté pour enlever votre like.");
        }
        User currentUser = (User) authentication.getPrincipal();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post introuvable avec l'ID : " + postId));

        Like like = likeRepository.findByUserAndPost(currentUser, post)
                .orElseThrow(() -> new ResourceNotFoundException("Like introuvable pour ce post et cet utilisateur."));
        likeRepository.delete(like);
    }
}
