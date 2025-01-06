package fr.univrouen.instalite.controllers;

import fr.univrouen.instalite.entities.Post;
import fr.univrouen.instalite.entities.Video;
import fr.univrouen.instalite.exceptions.ResourceNotFoundException;
import fr.univrouen.instalite.services.PostService;
import fr.univrouen.instalite.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/posts/videos")
public class VideoPostController {
    @Autowired
    private VideoService videoService;

    @Autowired
    private PostService postService;

    // On ajoute une video à un post
    @PostMapping("/upload/{postId}")
    @Transactional
    public ResponseEntity<?> uploadVideo(@PathVariable Long postId,
                                         @RequestParam("file") MultipartFile file) throws IOException {

        Optional<Post> postOptional = postService.getPostById(postId);
        if (postOptional.isEmpty()) {
            throw new ResourceNotFoundException("Le post avec l'ID " + postId + " n'a pas été trouvé.");
        }
        // On crée la vidéo
        Post post = postOptional.get();
        Video video = videoService.createFile(file, post);

        return ResponseEntity.status(HttpStatus.CREATED).body(video);
    }

    // On recupere la video avec l'ID en donnée
    @GetMapping("/{id}")
    public ResponseEntity<?> getVideo(@PathVariable Long id) throws IOException {
        Optional<Video> videoOptional = videoService.findById(id);
        if (videoOptional.isEmpty()) {
            throw new ResourceNotFoundException("La video avec l'ID " + id + " n'a pas été trouvé.");
        }

        Video video = videoOptional.get();
        byte[] fileData = videoService.getFile(video.getUrl());
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(fileData);
    }

    // On supprime la video par son ID
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteVideo(@PathVariable Long id) {
        Optional<Video> videoOpt = videoService.findById(id);
        if (videoOpt.isEmpty()) {
            throw new ResourceNotFoundException("La video avec l'ID " + id + " n'a pas été trouvé.");
        }

        Video video = videoOpt.get();
        boolean deleted = videoService.deleteFile(video.getUrl(), id);
        if (deleted) {
            return ResponseEntity.ok("Video Supprimé avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("La suppression de la video a echouée");
        }
    }

}
