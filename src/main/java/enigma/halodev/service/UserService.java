package enigma.halodev.service;

import enigma.halodev.dto.UserDTO;
import enigma.halodev.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<User> getAll(Pageable pageable);

    User getById(Long id);

    User updateById(Long id, UserDTO updatedUser);

    void deleteById(Long id);
}
