package fr.univrouen.instalite.controllers;

import fr.univrouen.instalite.dto.RegisterUser;
import fr.univrouen.instalite.entities.Picture;
import fr.univrouen.instalite.entities.Post;
import fr.univrouen.instalite.entities.User;
import fr.univrouen.instalite.entities.Video;
import fr.univrouen.instalite.exceptions.ResourceNotFoundException;
import fr.univrouen.instalite.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RequestMapping("/admin")
@RestController
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    

    @Autowired
    private UserService userService;

    @Autowired
    private PictureService pictureService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private PostService postService;

    @Autowired
    private AdminService adminService;

    // --- Gestion des Utilisateurs ------------------------------------------------------------------
    @GetMapping("/users")
    public ResponseEntity<List<User>> listUsers() {
        List<User> users = userService.allUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody RegisterUser registerUser) {
        User createdUser = adminService.createUser(registerUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Utilisateur supprimé avec succès.");
    }

    // --- Gestion des posts --------------------------------------------------------------

    @PostMapping("/posts")
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        Post createdPost = postService.createPost(post);
        return ResponseEntity.ok(createdPost);
    }

    @GetMapping(("/posts"))
    public ResponseEntity<List<Post>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Post>> getPostsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(postService.getPostsByUser(userId));
    }

    @GetMapping("users/{postId}")
    public ResponseEntity<?> getPostById(@PathVariable Long postId) {
        Optional<Post> post = postService.getPostById(postId);
        return post.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Le post avec l'ID " + postId + " n'a pas été trouvé."));
    }

    @DeleteMapping("users/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        boolean deleted = postService.deletePost(postId);
        if (deleted) {
            return ResponseEntity.ok("Post supprimé avec succès");
        } else {
            throw new ResourceNotFoundException("Le post avec ID " + postId + " n'est pas trouvé.");
        }
    }

    @PutMapping("users/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable Long postId, @RequestBody Post updatedPost) {
        Optional<Post> updated = postService.updatePost(postId, updatedPost);
        return updated.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Le post avec l'ID " + postId + " n'a pas été trouvé."));
    }

    // --- Gestion des Images --------------------------------------------------------------------
    @GetMapping("/images")
    public ResponseEntity<List<Picture>> listImages() {
        List<Picture> images = pictureService.allPictures();
        return ResponseEntity.ok(images);
    }

    @PostMapping("/images")
    @Transactional
    public ResponseEntity<?> uploadPicture(@PathVariable Long postId,
                                           @RequestParam("file") MultipartFile file, @RequestParam boolean isPrivate) throws IOException {
        Picture picture = pictureService.uploadPicture(postId, file, isPrivate);
        return ResponseEntity.status(HttpStatus.CREATED).body(picture);
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<?> getPicture(@PathVariable Long id) throws IOException {
        Picture picture = pictureService.getPicture(id);
        byte[] fileData = pictureService.getFile(picture.getUrl());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(fileData);
    }


    @DeleteMapping("/images/{id}")
    @Transactional
    public ResponseEntity<?> deletePicture(@PathVariable Long id) {
        boolean deleted = pictureService.deletePicture(id);
        if (deleted) {
            return ResponseEntity.ok("Image supprimée avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("La suppression de l'image a échoué.");
        }
    }


    // --- Gestion des Vidéos ------------------------------------------------------------
    @GetMapping("/videos")
    public ResponseEntity<List<Video>> listVideos() {
        List<Video> videos = videoService.allVides();
        return ResponseEntity.ok(videos);
    }

    @PostMapping("/videos/upload/{postId}")
    @Transactional
    public ResponseEntity<?> uploadVideo(@PathVariable Long postId,
                                         @RequestParam("file") MultipartFile file, @RequestParam boolean isPrivate) throws IOException {
        Video video = videoService.uploadVideo(postId, file, isPrivate);
        return ResponseEntity.status(HttpStatus.CREATED).body(video);
    }


    @DeleteMapping("videos/{id}")
    @Transactional
    public ResponseEntity<?> deleteVideo(@PathVariable Long id) {
        boolean deleted = videoService.deleteVideo(id);
        if (deleted) {
            return ResponseEntity.ok("Video Supprimé avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("La suppression de la video a echouée");
        }
    }
    

    @GetMapping("/videos/view/{id}")
    public ResponseEntity<?> getVideo(@PathVariable Long id) throws IOException {
        Video video = videoService.getVideo(id);
        byte[] fileData = videoService.getFile(video.getUrl());
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(fileData);
    }
//    ----------------------------------------------------------------------------------------

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userService.countUsers());
        stats.put("totalImages", pictureService.countImages());
        stats.put("totalVideos", videoService.countVideos());
        stats.put("imageFormats", pictureService.getFileFormats());
        stats.put("videoFormats", videoService.getFileFormats());
        return ResponseEntity.ok(stats);
    }
    
}
