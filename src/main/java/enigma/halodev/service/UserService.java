package enigma.halodev.service;

import enigma.halodev.dto.UserDTO;
import enigma.halodev.dto.UserDTO.TopUpDto;
import enigma.halodev.model.Programmer;
import enigma.halodev.model.Transaction;
import enigma.halodev.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    User getCurrentAuthenticatedUser(User user);

    User updateUser(User user, UserDTO userDTO);

    void changePassword(User user, UserDTO.ChangePasswordDTO changePasswordDTO);

    Transaction topUp(User user, Double amount);

    void delete(User user);

    User uploadProfilePicture(Authentication auth, MultipartFile image) throws IOException;

    void chargeUserAfterTransaction(User user, Double amount);

    void addProgrammerBalanceAfterTransaction(Programmer foundProgrammer, Double amount);
}
