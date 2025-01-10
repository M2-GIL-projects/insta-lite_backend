package fr.univrouen.instalite.repositories;

import fr.univrouen.instalite.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);

    List<Post> findByIsPrivate(boolean isPrivate);

    @Query("SELECT p FROM Post p WHERE p.isPrivate = :isPrivate AND p.user.id = :userId")
    List<Post> findByIsPrivateAndUserId(@Param("isPrivate") boolean isPrivate, @Param("userId") Long userId);


}
