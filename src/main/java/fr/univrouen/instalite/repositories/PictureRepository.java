package fr.univrouen.instalite.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.univrouen.instalite.entities.Picture;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {
    List<Picture> findByIsPrivate(boolean isPrivate);
}
