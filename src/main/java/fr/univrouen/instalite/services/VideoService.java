package fr.univrouen.instalite.services;

import fr.univrouen.instalite.entities.Post;
import fr.univrouen.instalite.entities.Video;
import fr.univrouen.instalite.repositories.VideoRepository;
import fr.univrouen.instalite.utils.MediaStorageUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class VideoService {
    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private MediaStorageUtil storageUtil;

    public byte[] getFile(String path) throws IOException {
        return storageUtil.getFile(path);
    }

    public Optional<Video> findById(Long id) {
        return videoRepository.findById(id);
    }

    public boolean deleteFile(String path,Long id){
        boolean deleted = storageUtil.deleteFile(path);
        if(deleted && videoRepository.existsById(id)){
            videoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public Video createFile(MultipartFile file, Post post) throws IOException {
        Video media = new Video();
        String path = storageUtil.createVideo(file);
        media.setUrl(path);
        media.setExtension(FilenameUtils.getExtension(media.getUrl()));
        media.setPost(post);
        return videoRepository.save(media);
    }

}
