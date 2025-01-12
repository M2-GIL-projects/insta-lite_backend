package fr.univrouen.instalite.repositories;

import fr.univrouen.instalite.entities.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;


public interface VideoRepository extends JpaRepository<Video, Long> {

    List<Video> findByIsPrivate(boolean isPrivate);


    @Query("SELECT DISTINCT v.extension FROM Video v")
    Set<String> getDistinctFileFormats();
}
