package fr.univrouen.instalite.controllers;

import fr.univrouen.instalite.dto.LoginResponse;
import fr.univrouen.instalite.dto.LoginUser;
import fr.univrouen.instalite.dto.PictureRequest;
import fr.univrouen.instalite.dto.RegisterUser;
import fr.univrouen.instalite.entities.Picture;
import fr.univrouen.instalite.entities.User;
import fr.univrouen.instalite.services.AuthenticationService;
import fr.univrouen.instalite.services.JwtService;
import fr.univrouen.instalite.services.PictureService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/pictures")
@RestController
public class PictureController {
    private final PictureService pictureService;

    public PictureController(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @PostMapping("/")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Picture> addPicture(@RequestBody PictureRequest pictureRequest) {
        Picture picture = pictureService.addPicture(pictureRequest);
        return ResponseEntity.ok(picture);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deletePicture(@PathVariable Long id) {
        pictureService.deletePicture(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/public")
    public ResponseEntity<List<Picture>> getPublicPictures() {
        List<Picture> publicPictures = pictureService.getPublicPictures();
        return ResponseEntity.ok(publicPictures);
    }

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Picture>> getAllPictures() {
        List<Picture> allPictures = pictureService.getAllPictures();
        return ResponseEntity.ok(allPictures);
    }

    @PatchMapping("/{id}/visibility")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Picture> updateVisibility(
            @PathVariable Long id,
            @RequestParam("isPrivate") boolean isPrivate
    ) {
        Picture updatedPicture = pictureService.updateVisibility(id, isPrivate);
        return ResponseEntity.ok(updatedPicture);
    }
}
