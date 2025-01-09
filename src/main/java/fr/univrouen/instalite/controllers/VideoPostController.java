package fr.univrouen.instalite.controllers;

import fr.univrouen.instalite.entities.Video;
import fr.univrouen.instalite.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/posts/videos")
public class VideoPostController {
    @Autowired
    private VideoService videoService;

    // On ajoute une video à un post
    @PostMapping("/upload/{postId}")
    @Transactional
    public ResponseEntity<?> uploadVideo(@PathVariable Long postId,
                                         @RequestParam("file") MultipartFile file, boolean isPrivate) throws IOException {
        Video video = videoService.uploadVideo(postId, file, isPrivate);
        return ResponseEntity.status(HttpStatus.CREATED).body(video);
    }

    // On recupere la video avec l'ID en donnée
    @GetMapping("/{id}")
    public ResponseEntity<?> getVideo(@PathVariable Long id) throws IOException {
        Video video = videoService.getVideo(id);
        byte[] fileData = videoService.getFile(video.getUrl());
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(fileData);
    }

    // On supprime la video par son ID
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteVideo(@PathVariable Long id) {
        boolean deleted = videoService.deleteVideo(id);
        if (deleted) {
            return ResponseEntity.ok("Video Supprimé avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("La suppression de la video a echouée");
        }
    }

}
