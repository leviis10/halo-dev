package enigma.halodev.service.implementation;

import enigma.halodev.dto.UserDTO;
import enigma.halodev.exception.UserNotFoundException;
import enigma.halodev.model.User;
import enigma.halodev.repository.UserRepository;
import enigma.halodev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

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
}
