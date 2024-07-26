package enigma.halodev.service;

import enigma.halodev.dto.UserDTO;
import enigma.halodev.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    Page<User> getAll(Pageable pageable);

    User getById(Long id);

    User updateById(Long id, UserDTO updatedUser);

    void deleteById(Long id);
    void updateBalance(Long id, Integer amount);

    User uploadProfilePicture(Authentication auth, MultipartFile image) throws IOException;
}
