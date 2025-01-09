package fr.univrouen.instalite.controllers;

import fr.univrouen.instalite.entities.Picture;
import fr.univrouen.instalite.entities.Post;
import fr.univrouen.instalite.exceptions.ResourceNotFoundException;
import fr.univrouen.instalite.services.PictureService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import fr.univrouen.instalite.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;


@RestController
@RequestMapping("/posts/pictures")
public class PicturePostController {

    @Autowired
    private PictureService pictureService;

    @Autowired
    private PostService postService;

    // On ajoute une image à un post
    @PostMapping("/upload/{postId}")
    @Transactional
    public ResponseEntity<?> uploadPicture(@PathVariable Long postId,
                                           @RequestParam("file") MultipartFile file) throws IOException {

        Optional<Post> postOptional = postService.getPostById(postId);
        if (postOptional.isEmpty()) {
            throw new ResourceNotFoundException("Le post avec l'ID " + postId + " n'a pas été trouvé.");
        }
        // On crée l'image
        Post post = postOptional.get();
        Picture picture = pictureService.createFile(file, post);

        return ResponseEntity.status(HttpStatus.CREATED).body(picture);
    }

    // On recupere l'image avec l'ID en donnée
    @GetMapping("/{id}")
    public ResponseEntity<?> getPicture(@PathVariable Long id) throws IOException {
        Optional<Picture> pictureOptional = pictureService.findById(id);
        if (pictureOptional.isEmpty()) {
            throw new ResourceNotFoundException("L'image avec l'ID " + id + " n'a pas été trouvée.");
        }

        Picture picture = pictureOptional.get();
        byte[] fileData = pictureService.getFile(picture.getUrl());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(fileData);
    }

    // On supprime l'image par son ID
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deletePicture(@PathVariable Long id) {
        Optional<Picture> pictureOptional = pictureService.findById(id);
        if (pictureOptional.isEmpty()) {
            throw new ResourceNotFoundException("L'image avec l'ID " + id + " n'a pas été trouvée.");
        }

        Picture picture = pictureOptional.get();
        boolean deleted = pictureService.deleteFile(picture.getUrl(), id);
        if (deleted) {
            return ResponseEntity.ok("Image supprimée avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("La suppression de l'image a échoué.");
        }
    }
}
