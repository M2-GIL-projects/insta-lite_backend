package fr.univrouen.instalite.repositories;

import fr.univrouen.instalite.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);
    List<Post> findByIsPrivate(boolean isPrivate);
}
