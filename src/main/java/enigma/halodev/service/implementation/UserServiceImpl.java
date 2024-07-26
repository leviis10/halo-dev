package enigma.halodev.service.implementation;

import enigma.halodev.dto.UserDTO;
import enigma.halodev.exception.UserNotFoundException;
import enigma.halodev.model.User;
import enigma.halodev.repository.UserRepository;
import enigma.halodev.service.CloudinaryService;
import enigma.halodev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public Page<User> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User updateById(Long id, UserDTO dto) {
        User foundUser = getById(id);
        foundUser.setFirstName(dto.getFirstName());
        foundUser.setLastName(dto.getLastName());
        foundUser.setUsername(dto.getUsername());
        foundUser.setEmail(dto.getEmail());

        return userRepository.save(foundUser);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void updateBalanceAfterTopUp(Long id, Double amount) {
        User foundUser = getById(id);
        foundUser.setBalance(foundUser.getBalance() + amount);
        userRepository.save(foundUser);
    }

    @Override
    public User updateBalanceUserAfterTransaction(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateBalanceProgrammerAfterTransaction(User programmer) {
        return userRepository.save(programmer);
    }

    @Override
    public User uploadProfilePicture(Authentication auth, MultipartFile image) throws IOException {
        User currentUser = (User) auth.getPrincipal();
        currentUser.setProfilePicture(cloudinaryService.upload(currentUser, image));
        return userRepository.save(currentUser);
    }
}
