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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProgrammerServiceImpl implements ProgrammerService {
    private final ProgrammerRepository programmerRepository;
    private final UserService userService;

    @Override
    public Programmer create(ProgrammerDTO dto) {
        User foundUser = userService.getById(dto.getUser_id());

        if(foundUser == null){
            throw new UserNotFoundException();
        }

        return programmerRepository.save(Programmer.builder()
                .user(foundUser)
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
        User foundUser = userService.getById(id);
        Programmer programmer = new Programmer();
        if(foundUser == null){
            throw new UserNotFoundException();
        } else{
            programmer.setUser(foundUser);
            programmer.setPrice(dto.getPrice());
        }

        return programmerRepository.save(programmer);
    }

    @Override
    public void deleteById(Long id) {
        programmerRepository.deleteById(id);
    }
}
