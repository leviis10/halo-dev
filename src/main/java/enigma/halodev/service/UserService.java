package enigma.halodev.service;

import enigma.halodev.dto.UserDTO;
import enigma.halodev.dto.UserDTO.topUpDto;
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

    User uploadProfilePicture(Authentication auth, MultipartFile image) throws IOException;

    void updateBalanceAfterTopUp(Long id, Double amount);

    User updateBalanceUserAfterTransaction(User user);

    User updateBalanceProgrammerAfterTransaction(User programmer);

    topUpDto topUp(Authentication auth, Double amount);
}
