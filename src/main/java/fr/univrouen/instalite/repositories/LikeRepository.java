package fr.univrouen.instalite.repositories;

import fr.univrouen.instalite.entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
