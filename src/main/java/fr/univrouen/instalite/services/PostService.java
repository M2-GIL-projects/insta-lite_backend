package fr.univrouen.instalite.services;

import fr.univrouen.instalite.entities.Post;
import fr.univrouen.instalite.entities.User;
import fr.univrouen.instalite.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Post createPost(Post post) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Vous devez être connecté pour créer un post.");
        }

        User currentUser = (User) authentication.getPrincipal();
        post.setUser(currentUser);

        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public List<Post> getPostsByUser(Long userId) {
        return postRepository.findByUserId(userId);
    }

    public List<Post> getPublicPosts() {
        return postRepository.findByIsPrivate(false);
    }

    public Optional<Post> getPostById(Long postId) {
        return postRepository.findById(postId);
    }

    public boolean deletePost(Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Vous devez être connecté pour supprimer un post.");
        }
        else {
            if (postRepository.existsById(postId)) {
                postRepository.deleteById(postId);
                return true;
            }
        }
        return false;
    }

    public Optional<Post> updatePost(Long postId, Post updatedPost) {
        Optional<Post> existingPost = postRepository.findById(postId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Vous devez être connecté pour modifier un post.");
        }else{
            User currentUser = (User) authentication.getPrincipal();
            if (existingPost.isPresent()) {
                Post post = existingPost.get();
                if(Objects.equals(currentUser.getEmail(), post.getUser().getEmail())){
                    post.setContent(updatedPost.getContent());
                    post.setPrivate(updatedPost.isPrivate());
                    return Optional.of(postRepository.save(post));
                }
            }
        }
        return Optional.empty();
    }
}
