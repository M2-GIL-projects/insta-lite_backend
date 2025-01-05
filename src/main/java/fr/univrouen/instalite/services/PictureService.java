package fr.univrouen.instalite.services;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.univrouen.instalite.dto.PictureRequest;
import fr.univrouen.instalite.entities.Picture;
import fr.univrouen.instalite.exceptions.ResourceNotFoundException;
import fr.univrouen.instalite.repositories.PictureRepository;

@Service
public class PictureService {
    private final PictureRepository pictureRepository;

    public PictureService(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

    public Picture addPicture(PictureRequest pictureRequest) {
        Picture picture = new Picture();
        picture.setTitle(pictureRequest.getTitle());
        picture.setUrl(pictureRequest.getUrl());
        picture.setPrivate(pictureRequest.isPrivate());
        return pictureRepository.save(picture);
    }

    public void deletePicture(Long id) {
        pictureRepository.deleteById(id);
    }

    public List<Picture> getPublicPictures() {
        return pictureRepository.findByIsPrivate(false);
    }

    public List<Picture> getAllPictures() {
        return pictureRepository.findAll();
    }

    public Picture updateVisibility(Long id, boolean isPrivate) {
        Picture picture = pictureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Picture not found"));
        picture.setPrivate(isPrivate);
        return pictureRepository.save(picture);
    }
}
