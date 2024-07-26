package enigma.halodev.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {
    String upload(String username, MultipartFile image) throws IOException;
}
