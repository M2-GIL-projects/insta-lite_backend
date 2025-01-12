package fr.univrouen.instalite.services;

import fr.univrouen.instalite.entities.Post;
import fr.univrouen.instalite.entities.User;
import fr.univrouen.instalite.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public List<Post> getmyPosts(Long userId) {
        return postRepository.findByUserId(userId, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public List<Post> getPostsByUser(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return postRepository.findByIsPrivateAndUserId(false, userId);
        }
        if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"))) {
            return postRepository.findByIsPrivateAndUserId(false, userId);
        }else if(authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_PRIVILEGED_USER")) ||
                authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))){
            return postRepository.findByUserId(userId, Sort.by(Sort.Direction.DESC, "createdAt"));
        }else{
            return postRepository.findByIsPrivateAndUserId(false, userId);
        }
    }

    public List<Post> getPostsByRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"))) {
            return postRepository.findByIsPrivate(false, Sort.by(Sort.Direction.DESC, "createdAt"));
        }else if(authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_PRIVILEGED_USER"))){
            return postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        }else if(authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))){
            return postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        }else{
            return postRepository.findByIsPrivate(false, Sort.by(Sort.Direction.DESC, "createdAt"));
        }

    }

    public List<Post> getPublicPosts() {
        return postRepository.findByIsPrivate(false, Sort.by(Sort.Direction.DESC, "createdAt"));
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
