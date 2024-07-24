package enigma.halodev.service.implementation;

import enigma.halodev.dto.ProgrammerDTO;
import enigma.halodev.exception.UserNotFoundException;
import enigma.halodev.model.Availability;
import enigma.halodev.model.Programmer;
import enigma.halodev.model.User;
import enigma.halodev.repository.ProgrammerRepository;
import enigma.halodev.service.ProgrammerService;
import enigma.halodev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProgrammerServiceImpl implements ProgrammerService {
    private final ProgrammerRepository programmerRepository;
    private final UserService userService;

    @Override
    public Programmer create(Authentication auth, ProgrammerDTO dto) {
        User user = (User) auth.getPrincipal();

        return programmerRepository.save(Programmer.builder()
                .user(user)
                .availability(Availability.AVAILABLE)
                .price(dto.getPrice())
                .build()
        );
    }

    @Override
    public Page<Programmer> getAll(Pageable pageable) {
        return programmerRepository.findAll(pageable);
    }

    @Override
    public Programmer getById(Long id) {
        return programmerRepository.findById(id).
                orElseThrow(UserNotFoundException::new);
    }

    @Override
    public Programmer updateById(Long id, ProgrammerDTO dto) {
        Programmer programmer = new Programmer();

        programmer.setPrice(dto.getPrice());

        return programmerRepository.save(programmer);
    }

    @Override
    public void deleteById(Long id) {
        programmerRepository.deleteById(id);
    }
}
