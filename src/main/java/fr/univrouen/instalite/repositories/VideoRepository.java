package fr.univrouen.instalite.repositories;

import fr.univrouen.instalite.entities.Video;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VideoRepository extends JpaRepository<Video, Long> {

}
