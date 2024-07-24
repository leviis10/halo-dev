package enigma.halodev.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;

public interface CloudinaryService {
    String upload(MultipartFile image) throws IOException;

    void delete(String publicId) throws IOException;
}
