package fr.univrouen.instalite.repositories;

import fr.univrouen.instalite.entities.Like;
import fr.univrouen.instalite.entities.Post;
import fr.univrouen.instalite.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    List<Like> findByPostId(Long postId);

    Optional<Like> findByUserAndPost(User user, Post post);
}
