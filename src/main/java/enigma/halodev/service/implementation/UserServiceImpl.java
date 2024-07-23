package enigma.halodev.service.implementation;

import enigma.halodev.dto.UserDTO;
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
    public User getById(Integer id) {
        return null;
    }

    @Override
    public User updateById(Integer id, UserDTO updatedUser) {
        return null;
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public User topup(Integer id, Integer amount) {
        return null;
    }
}
