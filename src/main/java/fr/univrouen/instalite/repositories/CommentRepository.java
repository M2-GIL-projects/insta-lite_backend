package fr.univrouen.instalite.repositories;

import fr.univrouen.instalite.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
