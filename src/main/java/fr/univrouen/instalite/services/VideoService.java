package fr.univrouen.instalite.services;

import fr.univrouen.instalite.entities.Post;
import fr.univrouen.instalite.entities.Video;
import fr.univrouen.instalite.exceptions.ResourceNotFoundException;
import fr.univrouen.instalite.repositories.VideoRepository;
import fr.univrouen.instalite.utils.MediaStorageUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class VideoService {
    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private MediaStorageUtil storageUtil;

    @Autowired
    private PostService postService;

    public byte[] getFile(String path) throws IOException {
        return storageUtil.getFile(path);
    }

    public Optional<Video> findById(Long id) {
        return videoRepository.findById(id);
    }

    public List<Video> allVides() {return videoRepository.findAll();}

    public long countVideos() {return videoRepository.count();}

    public boolean deleteFile(String path,Long id){
        boolean deleted = storageUtil.deleteFile(path);
        if(deleted && videoRepository.existsById(id)){
            videoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public Video createFile(MultipartFile file, Post post, boolean isPrivate) throws IOException {
        Video media = new Video();
        String path = storageUtil.createVideo(file);
        media.setUrl(path);
        media.setExtension(FilenameUtils.getExtension(media.getUrl()));
        media.setPrivate(isPrivate);
        media.setPost(post);
        return videoRepository.save(media);
    }

    @Transactional
    public Video uploadVideo(Long postId, MultipartFile file, boolean isPrivate) throws IOException {

        Optional<Post> postOptional = postService.getPostById(postId);
        if (postOptional.isEmpty()) {
            throw new ResourceNotFoundException("Le post avec l'ID " + postId + " n'a pas été trouvé.");
        }
        // On crée la vidéo
        Post post = postOptional.get();
        return createFile(file, post, isPrivate);
    }

    public Video getVideo(Long id){
        Optional<Video> videoOptional = findById(id);
        if (videoOptional.isEmpty()) {
            throw new ResourceNotFoundException("La video avec l'ID " + id + " n'a pas été trouvé.");
        }
        return videoOptional.get();
    }

    public boolean deleteVideo(Long id) {
        Optional<Video> videoOpt = findById(id);
        if (videoOpt.isEmpty()) {
            throw new ResourceNotFoundException("La video avec l'ID " + id + " n'a pas été trouvé.");
        }

        Video video = videoOpt.get();
        return deleteFile(video.getUrl(), id);
    }

    public Set<String> getFileFormats() {
        return videoRepository.getDistinctFileFormats();
    }

}
