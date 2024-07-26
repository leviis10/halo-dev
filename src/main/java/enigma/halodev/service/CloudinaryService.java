package enigma.halodev.service;

import enigma.halodev.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {
    String upload(User user, MultipartFile image) throws IOException;
}
