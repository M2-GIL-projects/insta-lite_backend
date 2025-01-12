package fr.univrouen.instalite.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import fr.univrouen.instalite.entities.Post;
import fr.univrouen.instalite.exceptions.ResourceNotFoundException;
import fr.univrouen.instalite.utils.MediaStorageUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.univrouen.instalite.entities.Picture;
import fr.univrouen.instalite.repositories.PictureRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PictureService {
    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private MediaStorageUtil storageUtil;

    @Autowired
    private PostService postService;

    public byte[] getFile(String path) throws IOException {
        return storageUtil.getFile(path);
    }

    public Optional<Picture> findById(Long id) {
        return pictureRepository.findById(id);
    }

    public List<Picture> allPictures() {
        return pictureRepository.findAll();
    }

    public long countImages() {
        return pictureRepository.count();
    }

    public boolean deleteFile(String path,Long id){
        boolean deleted = storageUtil.deleteFile(path);
        if(deleted && pictureRepository.existsById(id)){
            pictureRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public Picture createFile(MultipartFile file, Post post, boolean isPrivate) throws IOException {
        Picture media = new Picture();
        String path = storageUtil.createPicture(file);
        media.setUrl(path);
        media.setExtension(FilenameUtils.getExtension(media.getUrl()));
        media.setPrivate(isPrivate);
        media.setPost(post);
        return pictureRepository.save(media);
    }



    @Transactional
    public Picture uploadPicture(Long postId, MultipartFile file, boolean isPrivate) throws IOException {

        Optional<Post> postOptional = postService.getPostById(postId);
        if (postOptional.isEmpty()) {
            throw new ResourceNotFoundException("Le post avec l'ID " + postId + " n'a pas été trouvé.");
        }
        // On crée l'image
        Post post = postOptional.get();
        return createFile(file, post, isPrivate);
    }

    public Picture getPicture(Long id){
        Optional<Picture> pictureOptional = findById(id);
        if (pictureOptional.isEmpty()) {
            throw new ResourceNotFoundException("L'image avec l'ID " + id + " n'a pas été trouvée.");
        }

        return pictureOptional.get();
    }

    public Picture updatePicture(Long id, MultipartFile file, boolean isPrivate) throws IOException {
        Optional<Picture> pictureOptional = findById(id);
        if (pictureOptional.isEmpty()) {
            throw new ResourceNotFoundException("L'image avec l'ID " + id + " n'a pas été trouvée.");
        }
        Picture picture = pictureOptional.get();
        storageUtil.deleteFile(picture.getUrl());
        String path = storageUtil.createPicture(file);
        picture.setUrl(path);
        picture.setExtension(FilenameUtils.getExtension(picture.getUrl()));
        picture.setPrivate(isPrivate);
        return pictureRepository.save(picture);
    }

    public boolean deletePicture(Long id) {
        Optional<Picture> pictureOptional = findById(id);
        if (pictureOptional.isEmpty()) {
            throw new ResourceNotFoundException("L'image avec l'ID " + id + " n'a pas été trouvée.");
        }

        Picture picture = pictureOptional.get();
        return deleteFile(picture.getUrl(), id);
    }

    public Set<String> getFileFormats() {
        return pictureRepository.getDistinctFileFormats();
    }
}
