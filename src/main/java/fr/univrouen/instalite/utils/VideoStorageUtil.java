package fr.univrouen.instalite.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class VideoStorageUtil {

    @Value("${app.upload.dir}")
    private String uploadDir;

    public String createFile(MultipartFile file) throws IOException {
        String dirPath = uploadDir + File.separator +
                "Videos" + File.separator +
                LocalDate.now();

        String fileName = String.format(
                "%s.%s",
                UUID.randomUUID().toString(),
                FilenameUtils.getExtension(file.getOriginalFilename())
        );

        Path directory = Paths.get(dirPath);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        Path filePath = directory.resolve(fileName);
        Files.write(filePath, file.getBytes());
        return filePath.toString();
    }

    public byte[] getFile(String path) throws IOException {
        File file = new File(path);
        byte[] b = null;
        if (file.exists()) {
            b = FileUtils.readFileToByteArray(file);
        }
        return b;
    }

    public boolean deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                System.out.println("Fichier supprimé avec succès : " + path);
            } else {
                System.out.println("Échec de la suppression du fichier : " + path);
            }
            return deleted;
        } else {
            System.out.println("Le fichier n'existe pas : " + path);
            return false;
        }
    }

}
