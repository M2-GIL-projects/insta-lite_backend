package fr.univrouen.instalite.services;

import java.io.IOException;
import java.util.Optional;

import fr.univrouen.instalite.entities.Post;
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

    public byte[] getFile(String path) throws IOException {
        return storageUtil.getFile(path);
    }

    public Optional<Picture> findById(Long id) {
        return pictureRepository.findById(id);
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
    public Picture createFile(MultipartFile file, Post post) throws IOException {
        Picture media = new Picture();
        String path = storageUtil.createPicture(file);
        media.setUrl(path);
        media.setExtension(FilenameUtils.getExtension(media.getUrl()));
        media.setPost(post);
        return pictureRepository.save(media);
    }
}
